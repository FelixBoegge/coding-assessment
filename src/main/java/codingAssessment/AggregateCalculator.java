package codingAssessment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateCalculator {
	// ------------------------------------------------------------------------------------------------------------------------------------------
	// class-own attributes
	private static Map<String, Double> weights;
	private static List<Trade> allTrades;
	private static HashMap<LocalDate, HashMap<String, List<Trade>>> mappedTrades;
	private static HashMap<LocalDate, HashMap<String, HashMap<String, Double>>> results;

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// constructor to avoid instantiation
	private AggregateCalculator() {}
	
	// ------------------------------------------------------------------------------------------------------------------------------------------
	// getters and setters
	public static HashMap<LocalDate, HashMap<String, List<Trade>>> getMappedTrades() {
		return mappedTrades;
	}

	private static void setAllTrades(List<Trade> allTrades) {
		AggregateCalculator.allTrades = allTrades;
	}

	private static void setMappedTrades(HashMap<LocalDate, HashMap<String, List<Trade>>> mappedTrades) {
		AggregateCalculator.mappedTrades = mappedTrades;
	}
	
	private static void setResults() {
		AggregateCalculator.results = new HashMap<>();
	}
	
	public static void setWeights(Map<String, Double> weights) {
		AggregateCalculator.weights = weights;
	}
	

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// retrieve all distinct tickers
	public static HashSet<String> getAllTickers() {
		HashSet<String> distinctTickers = new HashSet<>();		// using a hashSet to only retrieve
		for (Trade trade : allTrades) {							// distinct tickers
			distinctTickers.add(trade.getCompanyTicker());		// we do not care for the order
		}
		return distinctTickers;
	}

	// retrieve all distinct dates
	public static List<LocalDate> getAllDates() {
		List<LocalDate> distinctDates = new ArrayList<>();		// using a list to retrieve all dates
		for (Trade trade : allTrades) {							// from all trades
			if (!distinctDates.contains(trade.getDate())) {		// date is not added if already in list
				distinctDates.add(trade.getDate());				// list provides ordered dates
			}
		}
		return distinctDates;
	}

	/* ------------------------------------------------------------------------------------------------------------------------------------------
	 	map all trades to a hashMap with keys = allDistinctDates
		values will be nested hashMap with keys = allDistinctTickers
		values of nested hashMap are lists of trades for that particular date and ticker
		
		this method should be called, in order to set the classes own static attributes
		'allTrades' and 'mappedTrades'
	 */
	
	public static HashMap<LocalDate, HashMap<String, List<Trade>>> mapTrades(List<Trade> allTrades) {

		HashMap<LocalDate, HashMap<String, List<Trade>>> tradesByDateAndTicker = new HashMap<>();
		setAllTrades(allTrades);

		for (LocalDate distinctDate : getAllDates()) {
			HashMap<String, List<Trade>> hashMapByDate = new HashMap<>();
			List<Trade> tradesByDate = allTrades.stream().filter(trade -> trade.getDate().equals(distinctDate))
					.collect(Collectors.toList());							// filter by dates
			for (String distinctTicker : getAllTickers()) {
				List<Trade> tradesByTicker = new ArrayList<>();				// for each distinct ticker, create a list of trades
				hashMapByDate.put(distinctTicker, tradesByTicker);			// and put into inner hashMap, even though it might stay empty
			}
			for (Trade trade : tradesByDate) {
				hashMapByDate.get(trade.getCompanyTicker()).add(trade);		// fill inner hashMap with trades for corresponding ticker
			}
			tradesByDateAndTicker.put(distinctDate, hashMapByDate);			// add inner hashMap into outer hashMap with date as key
		}
		setMappedTrades(tradesByDateAndTicker);
		return tradesByDateAndTicker;
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------------------
	// saves results of aggregate values to later use for INDEX calculation
	private static void saveResult(LocalDate date, String ticker, String valueType, double value) {
		if (results == null) {
			AggregateCalculator.setResults();
		}
		if (results.keySet().contains(date)) {
			if (results.get(date).keySet().contains(ticker)) {
				results.get(date).get(ticker).put(valueType, value);
			}else {
				results.get(date).put(ticker, new HashMap<String, Double>());
				results.get(date).get(ticker).put(valueType, value);
			}
		} else {
			results.put(date, new HashMap<String, HashMap<String, Double>>());
			results.get(date).put(ticker, new HashMap<String, Double>());
			results.get(date).get(ticker).put(valueType, value);
		}
	}

	// -------------------------------------- Calculate all values for all dates for all tickers and INDEX --------------------------------------
	// ------------------------------------------------------------------------------------------------------------------------------------------
	// retrieves open price for specific ticker and date
	public static double getOpenPrice(LocalDate date, String ticker) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double openPrice;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set openPrice to 0.0
			openPrice = 0.0;
		} else {
			// retrieve first price of corresponding list
			openPrice = tradesForGivenTickerAndDate.get(0).getPrice();
		}
		saveResult(date, ticker, "openPrice", openPrice);
		return openPrice;
	}
	
	// retrieves close price for specific ticker and date
	public static double getClosePrice(LocalDate date, String ticker) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double closePrice;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set closePrice to 0.0
			closePrice = 0.0;
		} else {
			// retrieve last price of corresponding list
			closePrice = tradesForGivenTickerAndDate.get(tradesForGivenTickerAndDate.size() - 1).getPrice();
		}
		saveResult(date, ticker, "closePrice", closePrice);
		return closePrice;
	}

	// retrieves highest price for specific ticker and date
	public static double getHighestPrice(LocalDate date, String ticker) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double highestPrice;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set highestPrice to 0.0
			highestPrice = 0.0;
		} else {
			// retrieve last price of corresponding list, sorted in ascending order
			Collections.sort(tradesForGivenTickerAndDate, (t1, t2) -> Double.compare(t1.getPrice(), t2.getPrice()));
			highestPrice = tradesForGivenTickerAndDate.get(tradesForGivenTickerAndDate.size() - 1).getPrice();
		}
		saveResult(date, ticker, "highestPrice", highestPrice);
		return highestPrice;
	}

	// retrieves lowest price for specific ticker and date
	public static double getLowestPrice(LocalDate date, String ticker) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double lowestPrice;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set lowestPrice to 0.0
			lowestPrice = 0.0;
		} else {
			// retrieve first price of corresponding list, sorted in ascending order
			Collections.sort(tradesForGivenTickerAndDate, (t1, t2) -> Double.compare(t1.getPrice(), t2.getPrice()));
			lowestPrice = tradesForGivenTickerAndDate.get(0).getPrice();
		}
		saveResult(date, ticker, "lowestPrice", lowestPrice);
		return lowestPrice;
	}

	// calculates daily traded volume for specific ticker and date
	public static double getDailyTradedVolume(LocalDate date, String ticker) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double dailyTradedVolume = 0.0;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set dailyTradedVolume to 0.0
			dailyTradedVolume = 0.0;
		} else {
			// calculate daily traded volume; sum of all (price * numSecurities)
			for (Trade trade : tradesForGivenTickerAndDate) {
				dailyTradedVolume += (trade.getPrice() * trade.getNumSecurities());
			}
		}
		saveResult(date, ticker, "dailyTradedVolume", dailyTradedVolume);
		return dailyTradedVolume;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// calculates open price for INDEX using the weights for specific date
	public static double getOpenPriceIndex(LocalDate date) {
		double openPriceIndex = 0.0;
		for (String ticker : getAllTickers()) {										// for all tickers
			double openPrice = results.get(date).get(ticker).get("openPrice");		// retrieve openPrice from results
			while (openPrice == 0.0) {												// as long as openPrice = 0.0 -> no value
				openPrice = results													// retrieve openPrice from
						.get(date.minus(1, ChronoUnit.DAYS))						// previous day
						.get(ticker)
						.get("openPrice");
			}
			openPriceIndex += openPrice * weights.get(ticker);						// sum up all openPrice times their weights
		}
		return openPriceIndex;
	}
	
	// calculates close price for INDEX using the weights for specific date
	public static double getClosePriceIndex(LocalDate date) {
		double closePriceIndex = 0.0;
		for (String ticker : getAllTickers()) {										// for all tickers
			double closePrice = results.get(date).get(ticker).get("closePrice");	// retrieve closePrice from results
			while (closePrice == 0.0) {												// as long as closePrice = 0.0 -> no value
				closePrice = results												// retrieve closePrice from
						.get(date.minus(1, ChronoUnit.DAYS))						// previous day
						.get(ticker)
						.get("closePrice");
			}
			closePriceIndex += closePrice * weights.get(ticker);					// sum up all closePrice times their weights
		}
		return closePriceIndex;
	}

	// calculates highest price for INDEX using the weights for specific date
	public static double getHighestPriceIndex(LocalDate date) {
		double highestPriceIndex = 0.0;
		for (String ticker : getAllTickers()) {											// for all tickers
			double highestPrice = results.get(date).get(ticker).get("highestPrice");	// retrieve highestPrice from results
			while (highestPrice == 0.0) {												// as long as highestPrice = 0.0 -> no value
				highestPrice = results													// retrieve highestPrice from
						.get(date.minus(1, ChronoUnit.DAYS))							// previous day
						.get(ticker)
						.get("highestPrice");	// retrieve highestPrice from
			}
			highestPriceIndex += highestPrice * weights.get(ticker);					// sum up all highestPrice times their weights
		}
		return highestPriceIndex;
	}

	// calculates lowest price for INDEX using the weights for specific date
	public static double getLowestPriceIndex(LocalDate date) {
		double lowestPriceIndex = 0.0;
		for (String ticker : getAllTickers()) {											// for all tickers
			double lowestPrice = results.get(date).get(ticker).get("lowestPrice");		// retrieve lowestPrice from results
			while (lowestPrice == 0.0) {												// as long as lowestPrice = 0.0 -> no value
				lowestPrice = results													// retrieve lowestPrice from
						.get(date.minus(1, ChronoUnit.DAYS))							// previous day
						.get(ticker)
						.get("lowestPrice");	// retrieve lowestPrice from
			}
			lowestPriceIndex += lowestPrice * weights.get(ticker);						// sum up all lowestPrice times their weights
		}
		return lowestPriceIndex;
	}

	// calculates daily traded volume for INDEX using the weights for specific date
	public static double getDailyTradedVolumeIndex(LocalDate date) {
		double dailyTradedVolumeIndex = 0.0;
		for (String ticker : getAllTickers()) {							// for all tickers
			double dailyTradedVolumePrice = results						// retrieve dailyTradedVolume from results
					.get(date)
					.get(ticker)
					.get("dailyTradedVolume");
			while (dailyTradedVolumePrice == 0.0) {						// as long as dailyTradedVolume = 0.0 -> no value
				dailyTradedVolumePrice = results						// retrieve dailyTradedVolume from
						.get(date.minus(1, ChronoUnit.DAYS))			// previous day
						.get(ticker)
						.get("dailyTradedVolume");
			}
			dailyTradedVolumeIndex += dailyTradedVolumePrice * weights.get(ticker);	// sum up all dailyTradedVolume times their weights
		}
		return dailyTradedVolumeIndex;
	}

}

package codingAssessment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateCalculator {
	// -------------------------------------------------------------------------------------------------------------
	// class-own attributes
	private static Map<String, Double> weights;
	private static List<Trade> allTrades;
	private static HashMap<LocalDate, HashMap<String, List<Trade>>> mappedTrades;
	private static HashMap<LocalDate, HashMap<String, HashMap<String, Double>>> results;

	// -------------------------------------------------------------------------------------------------------------
	// private constructor to avoid instantiation
	private AggregateCalculator() {}
	
	// -------------------------------------------------------------------------------------------------------------
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
	
	// -------------------------------------------------------------------------------------------------------------
	// retrieve all distinct tickers
	public static HashSet<String> getAllTickers() {
		// using a HashSet to only retrieve distinct tickers
		// we do not care for the order
		HashSet<String> distinctTickers = new HashSet<>();
		for (Trade trade : allTrades) {
			distinctTickers.add(trade.getCompanyTicker());
		}
		return distinctTickers;
	}

	// retrieve all distinct dates
	public static List<LocalDate> getAllDates() {
		// using a list to retrieve all dates from all trades
		List<LocalDate> distinctDates = new ArrayList<>();
		for (Trade trade : allTrades) {
			// date is not added if already in list
			if (!distinctDates.contains(trade.getDate())) {
				// list provides ordered dates
				distinctDates.add(trade.getDate());
			}
		}
		return distinctDates;
	}

	// -------------------------------------------------------------------------------------------------------------
	/* 	map all trades to a HashMap with keys = allDistinctDates
		values will be nested HashMap with keys = allDistinctTickers
		values of nested HashMap are lists of trades for that particular date and ticker
		
		this method should be called, in order to set the classes own static attributes
		'allTrades' and 'mappedTrades' */
	
	public static HashMap<LocalDate, HashMap<String, List<Trade>>> mapTrades(List<Trade> allTrades) {

		HashMap<LocalDate, HashMap<String, List<Trade>>> tradesByDateAndTicker = new HashMap<>();
		setAllTrades(allTrades);

		for (LocalDate distinctDate : getAllDates()) {
			HashMap<String, List<Trade>> hashMapByDate = new HashMap<>();
			// filter by dates
			List<Trade> tradesByDate = allTrades.stream().filter(trade -> trade.getDate().equals(distinctDate))
					.collect(Collectors.toList());
			for (String distinctTicker : getAllTickers()) {
				// for each distinct ticker, create a list of trades
				// and put into inner HashMap, even though it might stay empty
				List<Trade> tradesByTicker = new ArrayList<>();
				hashMapByDate.put(distinctTicker, tradesByTicker);
			}
			for (Trade trade : tradesByDate) {
				// fill inner HashMap with trades for corresponding ticker
				hashMapByDate.get(trade.getCompanyTicker()).add(trade);
			}
			// add inner HashMap into outer HashMap with date as key
			tradesByDateAndTicker.put(distinctDate, hashMapByDate);
		}
		setMappedTrades(tradesByDateAndTicker);
		return tradesByDateAndTicker;
	}
	
	// -------------------------------------------------------------------------------------------------------------
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

	// ----------------------- Calculate all values for all dates for all tickers and INDEX ------------------------
	// -------------------------------------------------------------------------------------------------------------
	public static double getAggregateValue(LocalDate date, String ticker, String valueType) {
		List<Trade> tradesForGivenTickerAndDate = AggregateCalculator.getMappedTrades().get(date).get(ticker);
		double aggregateValue;
		if (tradesForGivenTickerAndDate == null || tradesForGivenTickerAndDate.size() == 0) {
			// if there are not trades for given date and ticker, set aggregateValue to 0.0
			aggregateValue = 0.0;
		} else {
			switch (valueType) {
			case "openPrice":
				// retrieve first price of corresponding list
				aggregateValue = tradesForGivenTickerAndDate
						.get(0)
						.getPrice();
				break;
			case "closePrice":
				// retrieve last price of corresponding list
				aggregateValue = tradesForGivenTickerAndDate
						.get(tradesForGivenTickerAndDate.size() - 1)
						.getPrice();
				break;
			case "highestPrice":
				// retrieve last price of corresponding list, sorted in ascending order
				Collections.sort(
						tradesForGivenTickerAndDate,
						(t1, t2) -> Double.compare(t1.getPrice(), t2.getPrice()));
				aggregateValue = tradesForGivenTickerAndDate
						.get(tradesForGivenTickerAndDate.size() - 1)
						.getPrice();
				break;
			case "lowestPrice":
				// retrieve first price of corresponding list, sorted in ascending order
				Collections.sort(
						tradesForGivenTickerAndDate,
						(t1, t2) -> Double.compare(t1.getPrice(), t2.getPrice()));
				aggregateValue = tradesForGivenTickerAndDate
						.get(0)
						.getPrice();
				break;
			case "dailyTradedVolume":
				// calculate daily traded volume; sum of all (price * numSecurities)
				double dailyTradedVolume = 0.0;
				for (Trade trade : tradesForGivenTickerAndDate) {
					dailyTradedVolume += (trade.getPrice() * trade.getNumSecurities());
				}
				aggregateValue = dailyTradedVolume;
				break;
			default:
				aggregateValue = 0.0;
			}

		}
		saveResult(date, ticker, valueType, aggregateValue);
		return aggregateValue;
	}

	// -------------------------------------------------------------------------------------------------------------
	
	public static double getAggregateValueIndex(LocalDate date, String valueType) {
		double aggregateValueIndex = 0.0;
		// for all tickers
		for (String ticker : getAllTickers()) {
			// retrieve aggregateValue from results
			double aggregateValue = results.get(date).get(ticker).get(valueType);
			LocalDate previousRecordedDate = date;
			// as long as openPrice = 0.0 -> no value
			while (aggregateValue == 0.0) {
				// find last known day with records
				previousRecordedDate = getAllDates()
						.get(getAllDates().indexOf(previousRecordedDate) -1);
				// retrieve openPrice from last date with know records
				aggregateValue = results
						.get(previousRecordedDate)
						.get(ticker)
						.get(valueType);
			}
			// sum up all aggregateValues times their weights
			aggregateValueIndex += aggregateValue * weights.get(ticker);
		}
		return aggregateValueIndex;
	}
	
}

package codingAssessment;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		// path to csv-file. Must be changed on different machines
		String csvFilePath = "C:\\Users\\felix\\workspace_eclipse\\codingAssessment\\test-market.csv";
		Reader reader = new Reader();
		
		// read csv-file, construct a trade object for every record and put into allTrades list
		List<Trade> allTrades = reader.getAllTrades(csvFilePath);
		
		// provide weights
		Map<String, Double> weights = Map.of(
				"ABC", 0.1,
				"MEGA", 0.3,
				"NGL", 0.4,
				"TRX", 0.2
				);
		AggregateCalculator.setWeights(weights);

		// map all trades to a HashMap with keys = allDistinctDates
		// values will be nested HashMap with keys = allDistinctTickers
		// values of nested HashMap are lists of trades for that particular date and ticker
		// also sets AggregateCalculator's own allTrades list and mappedTrades HashMap
		AggregateCalculator.mapTrades(allTrades);

		print();
	}
	

	// retrieves/ calculates values for openPrice, closePrice, highestPrice, lowestPrice, dailyTradedVolume
	// for each given day and each ticker and INDEX
	// if there are no trades for a particular ticker, "N/A" is printed
	public static void print() {
		// displayed format for printed values
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		for (LocalDate dayKey : AggregateCalculator.getAllDates()) {
			System.out.println("_______________" + dayKey + "_______________");
			for (String tickerKey : AggregateCalculator.getMappedTrades().get(dayKey).keySet()) {
				System.out.println("***************** " + tickerKey + " ******************");
				double openPrice = AggregateCalculator.getAggregateValue(dayKey, tickerKey, "openPrice");
				double closePrice = AggregateCalculator.getAggregateValue(dayKey, tickerKey, "closePrice");
				double highestPrice = AggregateCalculator.getAggregateValue(dayKey, tickerKey, "highestPrice");
				double lowestPrice = AggregateCalculator.getAggregateValue(dayKey, tickerKey, "lowestPrice");
				double dailyTradedVolume = AggregateCalculator.getAggregateValue(dayKey, tickerKey, "dailyTradedVolume");

				if (openPrice == 0.0) { // -> no value
					System.out.println("Open price: N/A");
				} else {
					System.out.println("Open price: " + df.format(openPrice));
				}
				if (closePrice == 0.0) { // -> no value
					System.out.println("Close price: N/A");
				} else {
					System.out.println("Close price: " + df.format(closePrice));
				}
				if (highestPrice == 0.0) { // -> no value
					System.out.println("Highest price: N/A");
				} else {
					System.out.println("Highest price: " + df.format(highestPrice));
				}
				if (lowestPrice == 0.0) { // -> no value
					System.out.println("Lowest price: N/A");
				} else {
					System.out.println("Lowest price: " + df.format(lowestPrice));
				}
				if (dailyTradedVolume == 0.0) { // -> no value
					System.out.println("Daily traded volume: N/A\n");
				} else {
					System.out.println("Daily traded volume: " + df.format(dailyTradedVolume) + "\n");
				}
			}
			System.out.println("**************** INDEX *****************");
			double openPriceIndex = AggregateCalculator.getAggregateValueIndex(dayKey, "openPrice");
			double closePriceIndex = AggregateCalculator.getAggregateValueIndex(dayKey, "closePrice");
			double highestPriceIndex = AggregateCalculator.getAggregateValueIndex(dayKey, "highestPrice");
			double lowestPriceIndex = AggregateCalculator.getAggregateValueIndex(dayKey, "lowestPrice");
			double dailyTradedVolumeIndex = AggregateCalculator.getAggregateValueIndex(dayKey, "dailyTradedVolume");

			System.out.println("Open price: " + df.format(openPriceIndex));
			System.out.println("Close price: " + df.format(closePriceIndex));
			System.out.println("Highest price: " + df.format(highestPriceIndex));
			System.out.println("Lowest price: " + df.format(lowestPriceIndex));
			System.out.println("Daily traded volume: " + df.format(dailyTradedVolumeIndex) + "\n");
		}
	}
}

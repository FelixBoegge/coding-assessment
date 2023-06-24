package codingAssessment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testAggregateCalculator {
	
	@BeforeEach
	void setup() {
		String csvFilePath = "C:\\Users\\felix\\workspace_eclipse\\codingAssessment\\test-market.csv";
		Reader reader = new Reader();
		List<Trade> allTrades = reader.getAllTrades(csvFilePath);
		Map<String, Double> weights = Map.of(
				"ABC", 0.1,
				"MEGA", 0.3,
				"NGL", 0.4,
				"TRX", 0.2
				);
		AggregateCalculator.setWeights(weights);
		AggregateCalculator.mapTrades(allTrades);
	}

	@Test
	void test_getAllTickers_returns4Tickers_IfMarketLogIsPassed() {
		int numTickers = AggregateCalculator.getAllTickers().size();
		assertEquals(4, numTickers);
	}
	
	@Test
	void test_getAllTickers_returnsCorrectTickers_IfMarketLogIsPassed() {
		HashSet<String> allTickers = AggregateCalculator.getAllTickers();
		HashSet<String> trueAllTickers = new HashSet<String>();
		trueAllTickers.add("ABC");
		trueAllTickers.add("NGL");
		trueAllTickers.add("TRX");
		trueAllTickers.add("MEGA");
		assertEquals(trueAllTickers, allTickers);
	}
	
	@Test
	void test_getAllDates_returns7Dates_IfMarketLogIsPassed() {
		int numDates = AggregateCalculator.getAllDates().size();
		assertEquals(7, numDates);
	}
	
	@Test
	void test_getAllDates_returnsCorrectDates_IfMarketLogIsPassed() {
		List<LocalDate> allDates = AggregateCalculator.getAllDates();
		List<LocalDate> trueAllDates = new ArrayList<>();
		trueAllDates.add(LocalDate.parse("2023-06-01"));
		trueAllDates.add(LocalDate.parse("2023-06-02"));
		trueAllDates.add(LocalDate.parse("2023-06-05"));
		trueAllDates.add(LocalDate.parse("2023-06-06"));
		trueAllDates.add(LocalDate.parse("2023-06-07"));
		trueAllDates.add(LocalDate.parse("2023-06-08"));
		trueAllDates.add(LocalDate.parse("2023-06-09"));
		assertEquals(trueAllDates, allDates);
	}
	
	@Test
	void test_getOpenPrice_returnsCorrectOpenPrice_If20230609AndNGLIsPassedAsDateAndTickerRespectively() {
		double openPrice = AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-09"), "NGL");
		double trueOpenPrice = 4893.49;
		assertEquals(trueOpenPrice, openPrice);
	}
	
	@Test
	void test_getClosePrice_returnsCorrectClosePrice_If20230609AndNGLIsPassedAsDateAndTickerRespectively() {
		double closePrice = AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-09"), "NGL");
		double trueClosePrice = 4968.97;
		assertEquals(trueClosePrice, closePrice);
	}
	
	@Test
	void test_getOpenPriceIndex_returnsCorrectOpenPriceForINDEX_If20230609IsPassedAsDate() {
		AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-09"), "NGL");
		AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-09"), "ABC");
		AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-09"), "MEGA");
		AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-09"), "TRX");
		AggregateCalculator.getOpenPrice(LocalDate.parse("2023-06-08"), "TRX");
		double openPriceIndex = AggregateCalculator.getOpenPriceIndex(LocalDate.parse("2023-06-09"));
		double trueOpenPriceIndex = 4022.97;
		assertEquals(trueOpenPriceIndex, openPriceIndex, 0.01);
	}
	
	@Test
	void test_getClosePriceIndex_returnsCorrectClosePriceForINDEX_If20230609IsPassedAsDate() {
		AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-09"), "NGL");
		AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-09"), "ABC");
		AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-09"), "MEGA");
		AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-09"), "TRX");
		AggregateCalculator.getClosePrice(LocalDate.parse("2023-06-08"), "TRX");
		double closePrice = AggregateCalculator.getClosePriceIndex(LocalDate.parse("2023-06-09"));
		double trueClosePriceIndex = 4175.40;
		assertEquals(trueClosePriceIndex, closePrice, 0.01);
	}
	

}

package codingAssessment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class testReader {
	
	Reader reader;
	
	@BeforeEach
	void setup() {
		reader = new Reader();	
	}

	@Test
	void test_executeGetAllTrades_returnsListOfTrades() {
		String csvFilePath = "C:\\Users\\felix\\workspace_eclipse\\codingAssessment\\test-market.csv";
		assertEquals(reader.getAllTrades(csvFilePath).get(0).getClass(), Trade.class);
	}
	
	@Test
	void test_executeGetAllTrades_returns104465Trades() {
		String csvFilePath = "C:\\Users\\felix\\workspace_eclipse\\codingAssessment\\test-market.csv";
		int numTrades = reader.getAllTrades(csvFilePath).size();
		assertEquals(104465, numTrades);
	}

}

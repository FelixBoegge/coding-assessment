package codingAssessment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	public Reader() {}

	public List<Trade> getAllTrades(String filePath) {
		List<Trade> allTrades = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		File file = new File(filePath);			// create file-object from file path
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		String currentRecord = null;
		
		try {
			fileReader = new FileReader(file);	// create fileReader from file-object
			bufferedReader = new BufferedReader(fileReader);	// create bufferedReader from fileReader
			while ((currentRecord = bufferedReader.readLine())!=null) {
				/* -> split each line by delimiter ';' into:
				 * 		dateTime		[0]
				 * 		ticker			[1]
				 * 		price			[2]
				 * 		numSecurities	[3]
				 * -> create trade object with retrieved values
				 * -> add them to the allTrades list
				 */
				LocalDateTime currentDateTime = LocalDateTime.parse(currentRecord.split(";")[0], formatter);
				String currentCompanyTicker = currentRecord.split(";")[1];
				double currentPrice = Double.parseDouble(currentRecord.split(";")[2].replace(',', '.')); // replace ',' with '.' to handle as double
				int currentNumSecurities = Integer.parseInt(currentRecord.split(";")[3]);
				allTrades.add(new Trade(currentDateTime, currentCompanyTicker, currentPrice, currentNumSecurities));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allTrades;
	}

}

# Felix Bögge coding assessment for Deutsche Bank 

## Getting started

- open a git bash terminal
- browse to your local Eclipse workspace
- clone this repository with:
```
git clone https://git.fdmgroup.com/f-23-03/felix-boegge-coding-assessment.git
```
- in Eclipse, go to file and import an existing Maven project
- select the cloned repository
- in the main class, change csvFilePath to the path of the file to analize
- in the testAggregateCalculator class, change csvFilePath to the path of the file to analize
- run the project

***

# Aggregate Values Calculator for Trade Logs

## Description
The application reads data on trades from a .csv-file.
The file contains one trade each line and attributes like:
- date+time
- company ticker
- price
- number of securities traded

To read the file, the filepath is passed to a custom Reader object, which creates
a trade object for each record in its ´getAllTrades()´ method. It returns a list of all trades.

Any Market Log can be analized, as long as it follows the same format.

The utility-class `AggregateCalculator` helps to map the data and retrieve & calculate the desired values.
It needs to be configured with the´mapTrades()´ and ´setWeights()´ methods.
The former method maps all trades to a HashMap that holds all distinct dates
as keys and nested HashMaps as values. Those nested HashMaps hold all distinct company tickers as
keys and a list, of all trades for the corresponding company ticker and date, as values.
The latter sets the weights.

With the ´AggregateCalculator´ set, you can easily retrieve openPrice, closePrice, highestPrice and lowestPrice
and calculate the dailyTradedVolume by adding up all prices*numberOfSecurities for a specific date and ticker.
When those values are retrieved/ calculated, they are automatically saved to a results HashMap, that constructs
itsself.
As soon as all those values for a specific date are present in the results HashMap, the INDEX can be calculated.
If, for a specific date, there is no value for one of the tickers, the corresponding value from the previous
date with a value will be taken to calculate the INDEX.

The ´print()´ method calls the methods to retrieve/ calculate all wanted values and prints them to the screen.
If a value is 0.0, 'N/A' will be printed.
the print will have following structure:
		
		____________________date1___________________
		***************** ticker1 ******************
		Open price: value
		Close price: value
		Highest price: value
		Lowest price: value
		Daily traded volume: value
		
		***************** ticker2 ******************
		Open price: value
		Close price: value
		Highest price: value
		Lowest price: value
		Daily traded volume: value
		.
		.
		.
		____________________date2___________________
		***************** ticker1 ******************
		Open price: value
		Close price: value
		Highest price: value
		Lowest price: value
		Daily traded volume: value
		.
		.
		.

## Tests
You can run the project as JUnit Test
Two tests will test the Reader class

- if ´Reader.getAllTrades()´ returns a list of trade objects
- if ´Reader.getAllTrades()´ returns a list of 104,465 elements

8 tests will test the AggregateCalculator class

- if ´AggregateCalculator.getAllTickers()´ returns 4 tickers
- if ´AggregateCalculator.getAllTickers()´ returns the right tickers
- if ´AggregateCalculator.getAllDates()´ returns 7 dates
- if ´AggregateCalculator.getAllDates()´ returns the right dates
- if ´AggregateCalculator.getAggregateValue("openPrice")´ returns right value
- if ´AggregateCalculator.getAggregateValue("closePrice")´ returns right value
- if ´AggregateCalculator.getAggregateValueIndex("openPrice")´ returns right value
- if ´AggregateCalculator.getAggregateValueIndex("openPrice")´ returns right value
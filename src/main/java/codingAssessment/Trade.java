package codingAssessment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Trade {
	//-----------------------------------------------------------------------------------------------------
	// attributes
	private LocalDateTime dateTime;
	private String companyTicker;
	private double price;
	private int numSecurities;

	//-----------------------------------------------------------------------------------------------------
	// constructor
	public Trade(LocalDateTime dateTime, String companyTicker, double price, int numSecurities) {
		super();
		this.dateTime = dateTime;
		this.companyTicker = companyTicker;
		this.price = price;
		this.numSecurities = numSecurities;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// getters and setters
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public LocalDate getDate() {
		return dateTime.toLocalDate();
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getCompanyTicker() {
		return companyTicker;
	}

	public void setCompanyTicker(String companyTicker) {
		this.companyTicker = companyTicker;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNumSecurities() {
		return numSecurities;
	}

	public void setNumSecurities(int numSecurities) {
		this.numSecurities = numSecurities;
	}

	//-----------------------------------------------------------------------------------------------------
	@Override
	public int hashCode() {
		return Objects.hash(companyTicker, dateTime, numSecurities, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		return Objects.equals(companyTicker, other.companyTicker) && Objects.equals(dateTime, other.dateTime)
				&& numSecurities == other.numSecurities
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
	}

	@Override
	public String toString() {
		return dateTime + " - " + companyTicker + " - " + price + " - " + numSecurities;
	}

}

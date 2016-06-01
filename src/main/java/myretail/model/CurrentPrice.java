package myretail.model;

import org.mongodb.morphia.annotations.Embedded;

/**
 * A POJO to store a CurrentPrice's properties and the methods that operate on these properties
 *
 */
@Embedded
public class CurrentPrice {
	
	public static String VALUE_COLUMN_NAME = "current_price.value";
	public static String CURRENCY_CODE_COLUMN_NAME = "current_price.currency_code";
	private String value;
	private String currency_code; //TODO for future: Will be good to have an ENUM for the currency codes
	
	//Note: Can use builder pattern too, for now I am using traditional getters and setters
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency_code == null) ? 0 : currency_code.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrentPrice other = (CurrentPrice) obj;
		if (currency_code == null) {
			if (other.currency_code != null)
				return false;
		} else if (!currency_code.equals(other.currency_code))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}

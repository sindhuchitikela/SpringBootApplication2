package myretail.model;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class CurrentPrice {
	public static String VALUE_COLUMN_NAME = "current_price.value";
	public static String CURRENCY_CODE_COLUMN_NAME = "current_price.currency_code";
	private String value;
	private String currency_code;
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
	
}

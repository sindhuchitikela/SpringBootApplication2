package myretail.model;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProductCurrentPriceTest {

	@Test
	public void testProductSettersAndGetters(){
		String productId = "1234";
		String productName = "productname";
		String currencyCode = "USD";
		String priceValue = "1234";
		
		CurrentPrice price = new CurrentPrice();
		price.setCurrency_code(currencyCode);
		price.setValue(priceValue);
	
		Product product = new Product();
		product.setId(productId);
		product.setName(productName);
		product.setCurrent_price(price);
		
		assertEquals(price, product.getCurrent_price());
		assertEquals(productId, product.getId());
		assertEquals(productName, product.getName());			
	}
}

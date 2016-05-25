package myretail.controller;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import myretail.model.CurrentPrice;
import myretail.model.Products;

/**
 * To run these tests, Springboot application should be running. To run the
 * springboot app, mongodb should be running
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MyRetailApplication.class)
@WebIntegrationTest
public class IntegrationTests {
	final String BASE_URL = "http://localhost:8080/products/";

	@Test
	public void testAPIs() {
	
		//Prepare test data
		Products product1 = new Products();
		product1.setId("1111111");
		product1.setName("Some SMart TV");
		CurrentPrice price = new CurrentPrice();
		price.setCurrency_code("USD");
		price.setValue("123");
		product1.setCurrent_price(price);

		
		RestTemplate rest = new TestRestTemplate();

		ResponseEntity<Products> postResponse = rest.postForEntity(BASE_URL + "create", product1, Products.class,
				Collections.<String, Object> emptyMap());
		assertEquals(HttpStatus.OK, postResponse.getStatusCode());		


	}
	//TODO: Cleanup the data at the end
}
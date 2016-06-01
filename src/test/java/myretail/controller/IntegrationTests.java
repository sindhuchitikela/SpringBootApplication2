package myretail.controller;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import myretail.model.CurrentPrice;
import myretail.model.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MyRetailApplication.class)
@WebIntegrationTest
public class IntegrationTests {
	final String BASE_URL = "http://localhost:8080/products/";
	private static final String TEST_CURRENCY_CODE = "USD";

	private Product getTestData(String testProductId) {
		// Prepare test data
		Product product = new Product();
		product.setId(testProductId);
		CurrentPrice price = new CurrentPrice();
		price.setCurrency_code(TEST_CURRENCY_CODE);
		price.setValue("123");
		product.setCurrent_price(price);
		return product;
	}

	@Test
	public void testAPIs() {
		RestTemplate restTemplate = new TestRestTemplate();
		String testProductId = "13860421";
		Product productTestData = getTestData(testProductId);

		// ============Test POST call==========
		ResponseEntity<Product> postResponse = restTemplate.postForEntity(BASE_URL + "post", productTestData,
				Product.class, Collections.<String, Object> emptyMap());
		assertEquals(HttpStatus.OK, postResponse.getStatusCode());

		// =============Test GET call==========
		Product getProductsResponse = restTemplate.getForObject(BASE_URL + "?id=" + testProductId, Product.class);
		Client client = ClientBuilder.newClient();
		JsonParser json = new JsonParser();
		WebTarget resource = client.target("https://api.target.com/products/v3/" + testProductId
				+ "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		JsonObject jo = (JsonObject) json.parse(request.get(String.class));

		JsonArray items = jo.getAsJsonObject("product_composite_response").getAsJsonArray("items");
		String productName = items.get(0).getAsJsonObject().get("online_description").getAsJsonObject().get("value")
				.toString();
		productTestData.setName(productName);
		assertEquals(productTestData, getProductsResponse);

		// =============Test PUT call==========
		CurrentPrice price = new CurrentPrice();
		price.setCurrency_code(TEST_CURRENCY_CODE);
		price.setValue("5678");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", testProductId);
		HttpEntity<CurrentPrice> putEntity = new HttpEntity<CurrentPrice>(price);
		restTemplate.exchange(BASE_URL + "{id}", HttpMethod.PUT, putEntity, Void.class, testProductId);

		getProductsResponse = restTemplate.getForObject(BASE_URL + "?id=" + testProductId, Product.class);
		productTestData.setCurrent_price(price);
		productTestData.setName(productName);
		assertEquals(price, getProductsResponse.getCurrent_price());
	}

	@After
	public void cleanUp() {
		// TODO: cleanup test data after the tests.
	}
}
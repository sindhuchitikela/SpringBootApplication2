package myretail.controller;

import java.net.UnknownHostException;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;

import myretail.dao.ProductsDAO;
import myretail.model.CurrentPrice;
import myretail.model.Product;

/**
 * A RESTful API for get, post, put operations on Products
 * 
 *
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/products")
public class ProductsController {

	private ProductsDAO productsDao;
	private Datastore datastore;

	public ProductsController() throws UnknownHostException {
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			final Morphia morphia = new Morphia();
			morphia.map(Product.class, CurrentPrice.class);

			// initialize datastore and products DAO
			datastore = morphia.createDatastore(mongoClient, MyRetailApplication.DATABASE_NAME);
			productsDao = new ProductsDAO(mongoClient, morphia, MyRetailApplication.DATABASE_NAME);
		} catch (UnknownHostException e) {
			System.out.println("Exception occurred while establishing connection to MongoDB or creating the database");
			throw e;
		}
	}

	/**
	 * Saves the given product in the database
	 * 
	 * @param product
	 *            the product object to be stored in the database
	 * @return a map of successfully stored object details
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "post", method = RequestMethod.POST)
	public Map<String, Object> postProduct(@RequestBody Product product) throws UnknownHostException {

		// For the given ID, call an external API to get the name of the product
		Client client = ClientBuilder.newClient();
		JsonParser json = new JsonParser();
		WebTarget resource = client.target("https://api.target.com/products/v3/" + product.getId()
				+ "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		JsonObject jsonObject = (JsonObject) json.parse(request.get(String.class));
		JsonArray items = jsonObject.getAsJsonObject("product_composite_response").getAsJsonArray("items");
		String productName = items.get(0).getAsJsonObject().get("online_description").getAsJsonObject().get("value")
				.toString();

		// update the name on product instance
		product.setName(productName);
		datastore.save(product);

		Map<String, Object> postResponse = new LinkedHashMap<String, Object>();
		postResponse.put("message", "Product details are stored successfully");
		postResponse.put("product details saved", product);

		return postResponse;
	}

	/**
	 * Reads the product with a given id and returns it to the consumer
	 * 
	 * @param id
	 *            unique identifier of the product
	 * @return Product that is read from the database
	 * @throws UnknownHostException
	 *             thrown if there is an exception while reading data from the
	 *             database
	 */
	// Future Enhancements: For better performance, I would enhance this API to
	// accept a list of IDs and return a List<Products>
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public Product getProduct(@RequestParam(value = "id", required = true) String id) throws UnknownHostException {
		Query<Product> query = datastore.createQuery(Product.class);
		query.and(query.criteria("id").equal(id));
		return productsDao.find(query).get();
	}

	/**
	 * Updates the price of product with the given ID
	 * 
	 * @param id
	 *            unique identifier of the product
	 * @param price
	 *            the updated price of this product
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "{id}")
	public void putProduct(@PathVariable String id, @RequestBody CurrentPrice price) {
		final Query<Product> updateQuery = datastore.createQuery(Product.class);
		updateQuery.criteria("id").equal(id);
		UpdateOperations<Product> updateOperations = datastore.createUpdateOperations(Product.class)
				.set(CurrentPrice.VALUE_COLUMN_NAME, price.getValue())
				.set(CurrentPrice.CURRENCY_CODE_COLUMN_NAME, price.getCurrency_code());

		datastore.update(updateQuery, updateOperations);
	}
}
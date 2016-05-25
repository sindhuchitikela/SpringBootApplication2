package myretail.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;

import myretail.dao.ProductsDAO;
import myretail.model.CurrentPrice;
import myretail.model.Products;

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
			morphia.map(Products.class, CurrentPrice.class);

			// initialize datastore and metrics DAO
			datastore = morphia.createDatastore(mongoClient, MyRetailApplication.DATABASE_NAME);
			productsDao = new ProductsDAO(mongoClient, morphia, MyRetailApplication.DATABASE_NAME);
		} catch (UnknownHostException e) {
			System.out.println("Exception occurred while establishing connection to MongoDB or creating the database");
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Map<String, Object> createProducts(@RequestBody Products products) throws UnknownHostException {

		datastore.save(products);

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("message", "Product details are stored successfully");
		response.put("product details saved", products);

		return response;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<Products> readMetricsByTimeStamp(@RequestParam(value = "id", required = true) String id)
			throws UnknownHostException {
		Query<Products> query = datastore.createQuery(Products.class);
		query.and(query.criteria("id").equal(id));
		return prepareResponse(productsDao.find(query));
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public UpdateResults update(@RequestParam(value = "id", required = true) String id, @RequestBody CurrentPrice price) {
		final Query<Products> updateQuery = datastore.createQuery(Products.class);
		updateQuery.criteria("id").equal(id);
		UpdateOperations<Products> updateOperations = datastore.createUpdateOperations(Products.class).set(CurrentPrice.VALUE_COLUMN_NAME, price.getValue()).set(CurrentPrice.CURRENCY_CODE_COLUMN_NAME, price.getCurrency_code());
		
		return datastore.update(updateQuery, updateOperations);
		
	}

	private List<Products> prepareResponse(QueryResults<Products> allProductDetails) {
		List<Products> response = new ArrayList<Products>();
		for (Products retrieveProductDetails : allProductDetails) {
			response.add(retrieveProductDetails);
		}
		return response;
	}

}
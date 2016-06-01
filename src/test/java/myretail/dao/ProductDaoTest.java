package myretail.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.mockito.Mockito;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

import myretail.model.CurrentPrice;
import myretail.model.Product;

public class ProductDaoTest {
	@Test
	public void testProductDao() throws Exception {
		
		MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
		Morphia morphia = new Morphia();
		morphia.map(Product.class, CurrentPrice.class);		
		ProductDAO daoSpy = Mockito.spy(new ProductDAO(mongoClient, morphia, "test"));
		
		Query<Product> testQuery = Mockito.mock(Query.class);
		doReturn(testQuery).when(daoSpy).createQuery();
		
		Query<Product> returnedQuery = daoSpy.createQuery();
		assertEquals(testQuery, returnedQuery);
	}	
}

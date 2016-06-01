package myretail.dao;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;

import myretail.model.Product;
 
/**
 * 
 * For dataaccess operations on the database
 *
 */
public class ProductDAO extends BasicDAO<Product, String> {   
 
    public ProductDAO(MongoClient mongoClient, Morphia morphia, String dbName) {       
    	super(mongoClient, morphia, dbName);
    }     
}
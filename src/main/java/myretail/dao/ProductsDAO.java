package myretail.dao;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;

import myretail.model.Products;
 
public class ProductsDAO extends BasicDAO<Products, String> {   
 
    public ProductsDAO(MongoClient mongoClient, Morphia morphia, String dbName) {       
    	super(mongoClient, morphia, dbName);
    }     
}
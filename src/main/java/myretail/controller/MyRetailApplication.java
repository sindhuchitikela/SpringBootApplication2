package myretail.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyRetailApplication {
	public static String DATABASE_NAME = "MyRetailProducts";

	public static void main(String[] args) {
		SpringApplication.run(MyRetailApplication.class, args);
	}
}
package it.eng.alidalab.applicationcatalogue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationCatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationCatalogueApplication.class, args);
	}


}

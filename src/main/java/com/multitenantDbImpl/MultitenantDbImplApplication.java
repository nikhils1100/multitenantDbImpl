package com.multitenantDbImpl;

import com.multitenantDbImpl.controller.BookController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class MultitenantDbImplApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultitenantDbImplApplication.class, args);

		BookController bookController = new BookController();

		bookController.connectToDb("dev");
	}

}

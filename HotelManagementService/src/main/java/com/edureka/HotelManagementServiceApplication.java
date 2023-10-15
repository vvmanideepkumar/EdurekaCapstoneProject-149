package com.edureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HotelManagementServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext cxt = SpringApplication.run(HotelManagementServiceApplication.class, args);
	}

}

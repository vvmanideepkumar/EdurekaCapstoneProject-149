package com.edureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext cxt = SpringApplication.run(ReservationServiceApplication.class, args);
	}

}

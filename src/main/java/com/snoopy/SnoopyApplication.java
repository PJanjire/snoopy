package com.snoopy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnoopyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnoopyApplication.class, args);
		System.out.println("Welcome to Snoopy... ");
	}

}

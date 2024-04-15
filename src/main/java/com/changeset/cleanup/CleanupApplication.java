package com.changeset.cleanup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CleanupApplication {

	public static void main(String[] args) {
		System.out.println("Started......");
		SpringApplication.run(CleanupApplication.class, args);
	}

}

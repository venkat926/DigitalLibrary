package org.kvn.DigitalLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalLibraryApplication.class, args);

		System.out.println("""
                    Server started on port 8080. Use this URL to access:
                    http://localhost:8080/book
                    """);
	}


}

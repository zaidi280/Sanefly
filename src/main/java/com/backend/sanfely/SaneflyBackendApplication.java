package com.backend.sanfely;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SaneflyBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> 
            System.setProperty(entry.getKey(), entry.getValue())
        );
		SpringApplication.run(SaneflyBackendApplication.class, args);
	}

}

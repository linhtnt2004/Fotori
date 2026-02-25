package com.example.fotori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FotoriApplication {

	public static void main(String[] args) {
		SpringApplication.run(FotoriApplication.class, args);
	}

}

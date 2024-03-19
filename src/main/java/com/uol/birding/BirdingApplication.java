package com.uol.birding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@CrossOrigin
@SpringBootApplication
public class BirdingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirdingApplication.class, args);
	}
}

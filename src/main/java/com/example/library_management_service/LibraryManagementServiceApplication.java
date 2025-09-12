package com.example.library_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LibraryManagementServiceApplication extends SpringBootServletInitializer {  // <-- thêm extends

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementServiceApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LibraryManagementServiceApplication.class);
	}
}

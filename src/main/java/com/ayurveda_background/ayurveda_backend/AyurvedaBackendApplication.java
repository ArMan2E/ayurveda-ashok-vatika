package com.ayurveda_background.ayurveda_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AyurvedaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AyurvedaBackendApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager add(MongoDatabaseFactory databaseFactory){
		return new MongoTransactionManager(databaseFactory);
	}

}

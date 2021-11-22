package com.company.SAP_Project;

import com.company.SAP_Project.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SapProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SapProjectApplication.class, args);
	}

}

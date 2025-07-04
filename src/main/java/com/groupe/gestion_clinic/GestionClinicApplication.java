package com.groupe.gestion_clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestionClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionClinicApplication.class, args);
	}

}

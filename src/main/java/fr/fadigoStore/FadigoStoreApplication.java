package fr.fadigoStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableJpaAuditing
@EnableAsync
public class FadigoStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FadigoStoreApplication.class, args);
	}

}

package gov.tangerangkota.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TokenThlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenThlApplication.class, args);
	}

}

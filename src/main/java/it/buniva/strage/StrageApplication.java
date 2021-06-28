package it.buniva.strage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class StrageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrageApplication.class, args);
	}

}

package platform.game.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import platform.game.security.CreateToken;

@SpringBootApplication
@ComponentScan(basePackages = {"platform.game.controller","platform.game.env.config"})
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}


}

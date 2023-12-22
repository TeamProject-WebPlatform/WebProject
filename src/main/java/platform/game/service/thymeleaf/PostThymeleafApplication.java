package platform.game.service.thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"platform.game.model", "platform.game.action", "platform.game.entity"})
public class PostThymeleafApplication {
    
    public static void main(String[] args) {
		SpringApplication.run(PostThymeleafApplication.class, args);
	}
}

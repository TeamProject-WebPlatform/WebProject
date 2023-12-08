package platform.game.env.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource("classpath:properties/env.properties")
}) // evn.properties 등록
public class PropertyConfig {
    
}

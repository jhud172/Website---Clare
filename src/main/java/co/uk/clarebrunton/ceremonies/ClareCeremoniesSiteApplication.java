package co.uk.clarebrunton.ceremonies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClareCeremoniesSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClareCeremoniesSiteApplication.class, args);
	}

}

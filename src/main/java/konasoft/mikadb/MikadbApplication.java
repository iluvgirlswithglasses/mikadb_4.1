package konasoft.mikadb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MikadbApplication {
	public static final String VERSION = "4.0";

	public static void main(String[] args) {
		SpringApplication.run(MikadbApplication.class, args);
	}
}

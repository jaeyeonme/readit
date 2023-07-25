package me.jaeyeon.readitapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"me.jaeyeon.readitdomain"})
@EnableJpaRepositories(basePackages = {"me.jaeyeon.readitdomain"})
@ComponentScan(basePackages = {"me.jaeyeon.readitapi", "me.jaeyeon.readitdomain"})
public class ReaditApiApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application,application-domain");
		SpringApplication.run(ReaditApiApplication.class, args);
	}
}

package me.jaeyeon.readitapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"me.jaeyeon.readitapi", "me.jaeyeon.readitdomain"})
public class ReaditApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReaditApiApplication.class, args);
	}

}

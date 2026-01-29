package me.moye.hew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HewApplication {

	public static void main(String[] args) {
		SpringApplication.run(HewApplication.class, args);
	}

}

package com.epam.ld.module2.testing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SpringBootMessengerApplication implements CommandLineRunner {

	private final Messenger messenger;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringBootMessengerApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 0 || args.length == 2) {
			log.info("Start sending notification process");
			messenger.sendNotification(args);
		} else {
			log.info("Incorrect number of arguments");
		}
	}

}

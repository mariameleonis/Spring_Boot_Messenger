package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootMessengerApplicationTests {

	@Autowired
	private Messenger messenger;

	@Test
	void contextLoads() {
		assertNotNull(messenger);
	}

}

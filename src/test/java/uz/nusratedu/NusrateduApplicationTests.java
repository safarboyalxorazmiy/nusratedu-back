package uz.nusratedu;// src/test/java/uz/nusratedu/NusrateduApplicationTests.java

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // uses application-test.yml
class NusrateduApplicationTests {

	@Test
	void contextLoads() {
		// This test passes if context starts
	}
}
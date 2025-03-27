package org.tqs.deti.ua.test_containers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TestContainersApplicationTests {

	@Test
	void contextLoads() {
	}

}

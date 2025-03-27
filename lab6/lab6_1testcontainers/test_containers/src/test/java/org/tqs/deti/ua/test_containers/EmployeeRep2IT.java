package org.tqs.deti.ua.test_containers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.tqs.deti.ua.test_containers.Employee;
import org.tqs.deti.ua.test_containers.EmployeeRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeRep2IT {

    @Container
    public static final PostgreSQLContainer container = new PostgreSQLContainer<>()
            .withUsername("user")
            .withPassword("pass123")
            .withDatabaseName("test");

    @Autowired
    private EmployeeRepository employeeRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @Order(1)
    void testRetrieveEmployees() {
        Employee employee = employeeRepository.findAll().get(0);
        assertEquals(employee.getName(), "Alice Johnson"); // Based on our Flyway script
    }

    @Test
    @Order(2)
    void updateEmployee() {
        Employee employee = employeeRepository.findAll().get(0);
        employee.setName("Sung Jin-Woo");
        employeeRepository.save(employee);
        Employee employee2 = employeeRepository.findById(employee.getId()).get();
        assertEquals("Sung Jin-Woo", employee2.getName());
    }
}

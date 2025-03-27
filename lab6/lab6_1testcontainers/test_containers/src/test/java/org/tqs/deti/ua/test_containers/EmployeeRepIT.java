package org.tqs.deti.ua.test_containers;

import org.tqs.deti.ua.test_containers.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
class EmployeeRepIT {

    @Container
    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:12")
            .withUsername("user")
            .withPassword("pass123")
            .withDatabaseName("test");

    @Autowired
    private EmployeeRepository employeeRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @Order(1)
    void testInsertEmployee() {
        Employee employee = new Employee("Alice Johnson");
        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee.getId());
    }

    @Test
    @Order(2)
    void testRetrieveEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        assertFalse(employees.isEmpty());
        assertEquals("Alice Johnson", employees.get(0).getName());
    }

    @Test
    @Order(3)
    void testUpdateEmployee() {
        Employee employee = employeeRepository.findAll().get(0);
        employee.setName("Bob Smith");
        employeeRepository.save(employee);

        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        assertEquals("Bob Smith", updatedEmployee.getName());
    }

    @Test
    @Order(4)
    void testDeleteEmployee() {
        Employee employee = employeeRepository.findAll().get(0);
        employeeRepository.delete(employee);

        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());
        assertTrue(deletedEmployee.isEmpty());
    }
}

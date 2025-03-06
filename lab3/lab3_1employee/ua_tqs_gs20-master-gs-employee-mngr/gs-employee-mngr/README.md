## a) Identify a couple of examples that use AssertJ expressive methods chaining.

### Some examples in A_EmployeeRepositoryTest.java

```java
assertThat(found).isNotNull().
                extracting(Employee::getName).isEqualTo(persistedAlex.getName());
```

```java
assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
```

### Some examples in B_EmployeeService_UnitTest.java

```java
assertThat(allEmployees).hasSize(3).extracting(Employee::getName).contains(alex.getName(), john.getName(), bob.getName());
```

## b) Take note of transitive annotations included in @DataJpaTest.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@BootstrapWith(DataJpaTestContextBootstrapper.class)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@OverrideAutoConfiguration(enabled = false)
@ImportAutoConfiguration
@Transactional
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase
@AutoConfigureCache
```

## c) Identify an example in which you mock the behavior of the repository (and avoid involving a database).

```java
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class B_EmployeeService_UnitTest {

    // mocking the responses of the repository (i.e., no database will be used)
    // lenient is required because we load more expectations in the BeforeEach
    // than those used in some tests. As an alternative, the expectations
    // could move into each test method and be trimmed (no need for lenient then)
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp() {

        //these expectations provide an alternative to the use of the repository
        Employee john = new Employee("john", "john@deti.com");
        john.setId(111L);

        Employee bob = new Employee("bob", "bob@deti.com");
        Employee alex = new Employee("alex", "alex@deti.com");

        List<Employee> allEmployees = Arrays.asList(john, bob, alex);

        Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
        Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
        Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
        Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
        Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
    }
```

## d) What is the difference between standard @Mock and @MockBean?

@Mock is a Mockito annotation for creating mock instances in unit tests, not managed by Spring, while @MockBean is a Spring Boot annotation that replaces a bean in the application context, making it useful for integration tests.

## e) What is the role of the file “application-integrationtest.properties”? In which conditions will it be used?

This file overrides default properties when running integration tests and it provides a separate database configuration for running the integration tests. Spring Boot will only load this file during integration tests when the tests are executed with Spring Boot test framework.

## f) the sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences?

Firstly C test is unit test, while the other two are integration tests. C mocks the Employee Service (using @MockBean), D web server is mocked using @AutoCOnfigureMockMvc while in E the web server is actually being used.

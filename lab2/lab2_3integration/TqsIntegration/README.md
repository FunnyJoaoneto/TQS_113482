#f)

```bash
$ mvn test
```

Run the tests of ProductFinderServiceTest
Uses Surefire Plugin, which runs test classes matching the pattern \*Test.java
Integration tests are not executed in this phase.

```bash
$ mvn package
```

Runned the same tests as the command above + build
Same as above

```bash
$ mvn package -DskipTests=true
```

Run no tests + build
Skips all tests

```bash
$ mvn failsafe:integration-test
```

Run the tests of ProductFinderServiceIT
Uses Failsafe Plugin instead of Surefire.
Unit tests (\*Test.java) are not executed.

```bash
$ mvn install
```

Run the tests of Product FunderServiceTest + build
Same as mvn test
Integration tests are not executed by default

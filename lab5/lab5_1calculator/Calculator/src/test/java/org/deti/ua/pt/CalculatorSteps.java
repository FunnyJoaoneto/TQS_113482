package org.deti.ua.pt;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatorSteps {

    static final Logger log = getLogger(lookup().lookupClass());

    private Calculator calc;

    @Given("a calculator I just turned on")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        log.debug("Adding {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }

    @When("I subtract {int} to {int}")
    public void subtract(int arg1, int arg2) {
        log.debug("Subtracting {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }

    @When("I multiply {int} by {int}")
    public void multiply(int arg1, int arg2) {
        log.debug("Multiplying {} by {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("*");
    }

    @When("I divide {int} by {int}")
    public void divide(int arg1, int arg2) {
        log.debug("Dividing {} by {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("/");
    }

    @When("I perform an invalid operation with {int} and {int}")
    public void invalidOperation(int arg1, int arg2) {
        log.debug("Performing invalid operation with {} and {}", arg1, arg2);
        try {
            calc.push(arg1);
            calc.push(arg2);
            calc.push("?"); // Invalid operation
        } catch (Exception e) {
            log.debug("Caught exception: {}", e.getMessage());
        }
    }

    @Then("the result is {int}")
    public void the_result_is(double expected) {
        Number value = calc.value();
        log.debug("Result: {} (expected {})", value, expected);
        assertEquals(expected, value);
    }

    @Then("the result is an error")
    public void the_result_is_an_error() {
        log.debug("Checking for an error result");
        Exception exception = calc.getLastException();
        assertEquals(true, exception != null, "Expected an error but none occurred");
    }

}
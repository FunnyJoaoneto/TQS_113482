package org.tqs.deti.ua.homework.Cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;

public class TestSteps {
    private WebDriver driver;

    private String RESCODE = "RES-11111111111111111";

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/web/restaurants");
        driver.manage().window().setSize(new Dimension(929, 1053));
    }

    @Given("I open the restaurant page")
    public void iOpenTheRestaurantPage() {
        assertTrue(driver.getTitle().contains("Restaurants"));
    }

    @When("I click on {string}")
    public void iClickOnRestaurant(String restaurantName) {
        // Click on the link for the restaurant (Guimarães Tavern)
        driver.findElement(By.linkText(restaurantName)).click();
    }

    @Then("I should see the {string} page")
    public void iShouldSeeThePage(String pageTitle) {
        // Assert that the page title is correct
        assertEquals(pageTitle, driver.getTitle());
    }

    @And("the restaurant name should be {string}")
    public void theRestaurantNameShouldBe(String restaurantName) {
        // Assert that the restaurant name is displayed on the page
        assertTrue(driver.findElement(By.xpath("//h2/span")).getText().contains(restaurantName));
    }

    @When("I select a meal and specify the quantity")
    public void iSelectMealAndSpecifyQuantity() {
        // Select the first meal and set the quantity
        driver.findElement(By.id("meal-21")).click();
        WebElement quantityInput = driver.findElement(By.name("quantities[21]"));
        quantityInput.clear();
        quantityInput.sendKeys("3");
    }

    @And("I choose a time slot")
    public void iChooseATimeSlot() {
        // Select a time slot for the reservation
        driver.findElement(By.cssSelector("tr:nth-child(5) > td:nth-child(2) > .slot-btn")).click();
    }

    @And("I set the number of people to {int}")
    public void iSetTheNumberOfPeople(int numPeople) {
        // Set the number of people for the reservation
        WebElement peopleInput = driver.findElement(By.id("people"));
        peopleInput.clear();
        peopleInput.sendKeys(String.valueOf(numPeople));
    }

    @And("I click {string}")
    public void iClickOnButton(String buttonText) {
        // Click the button for reservation
        driver.findElement(By.xpath("//button[contains(text(),'" + buttonText + "')]")).click();
    }

    @Then("I should see the reservation confirmation")
    public void iShouldSeeTheReservationConfirmation() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirmationMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//h3")));
        // Assert that the reservation was successful
        assertTrue(driver.findElement(By.xpath("/html/body/div/h2")).getText().contains("Reservation Successful!"));
        String reservationCode = driver.findElement(By.xpath("/html/body/div/h3")).getText();
        RESCODE = reservationCode;
    }

    @When("I click go back to the restaurant page")
    public void iClickGoBackToTheRestaurantPage() {
        driver.findElement(By.xpath("//a[contains(text(),'← Back to Restaurants')]")).click(); // Adjust based on actual
                                                                                               // button text or link
    }

    @Then("I should see the restaurant page")
    public void iShouldSeeToTheRestaurantPage() {
        assertTrue(driver.getTitle().contains("Restaurants"));
    }

    @When("I search for the reservation using the code")
    public void iSearchForTheReservationUsingTheCode() {
        // Search for the reservation using the code
        driver.findElement(By.name("code")).sendKeys(RESCODE);
        driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();
    }

    @Then("I should see the reservation details")
    public void iShouldSeeTheReservationDetails() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirmationMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'" + RESCODE + "')]")));
        // Assert that the reservation details page is displayed
        assertTrue(driver.findElement(By.xpath("//span[contains(text(),'" + RESCODE + "')]")).getText()
                .contains(RESCODE));
    }

    @When("I check-in the reservation")
    public void iCheckInTheReservation() {
        // Click the check-in button for the reservation
        driver.findElement(By.cssSelector(".check-in")).click();
    }
}

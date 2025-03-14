package org.deti.ua.pt;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.*;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

public class CoverSteps {
    private WebDriver driver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://cover-bookstore.onrender.com/");
        driver.manage().window().setSize(new Dimension(929, 1053));
    }

    @Given("I am on the library homepage")
    public void iAmOnTheLibraryHomepage() {
        assertTrue(driver.getTitle().contains("Cover - Find your favorite books."));
    }

    @When("I enter {string} in the search bar")
    public void iEnterInTheSearchBar(String searchText) {
        WebElement searchBox = driver
                .findElement(
                        By.cssSelector(".Navbar_searchBarContainer__3UbnF > div:nth-child(1) > input:nth-child(1)"));
        searchBox.clear();
        searchBox.sendKeys(searchText);
    }

    @When("I click the search button")
    public void iClickTheSearchButton() {
        WebElement searchButton = driver.findElement(
                By.cssSelector(".Navbar_searchBarContainer__3UbnF > div:nth-child(1) > button:nth-child(2)"));
        searchButton.click();
    }

    @Then("I should see a list of {int} book(s)")
    public void iShouldSeeAListOfBooks(int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        try {
            wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".SearchList_searchBookCard__AnSAs")));
            List<WebElement> books = driver.findElements(By.cssSelector(".SearchList_searchBookCard__AnSAs"));
            assertEquals(expectedCount, books.size(), "The number of books found does not match the expected count.");
        } catch (TimeoutException e) {
            assertEquals(expectedCount, 0);
        }
    }

    @Then("I should see the {string} header")
    public void iShouldSeeTheCategoryHeader(String categoryHeader) {
        // Use WebDriverWait to ensure the header is present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        try {
            WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(text(),'" + categoryHeader + "')]")));
            assertTrue(header.isDisplayed(), "The header '" + categoryHeader + "' is not displayed on the page.");
        } catch (TimeoutException e) {
            fail("Timed out waiting for the header '" + categoryHeader + "' to be visible.");
        }
    }

    @Then("the book should have the title {string}")
    public void theBookShouldHaveTheTitle(String expectedTitle) {
        List<WebElement> bookTitles = driver.findElements(By.cssSelector(".SearchList_bookTitle__1wo4a"));
        boolean found = bookTitles.stream().anyMatch(e -> e.getText().equalsIgnoreCase(expectedTitle));
        assertTrue(found, "Expected book title not found: " + expectedTitle);
    }

    @When("I click on {string} in the category filter")
    public void iClickOnCategoryInTheFilter(String category) {
        WebElement categoryLink = driver.findElement(By.xpath("//span[contains(text(),'" + category + "')]"));
        categoryLink.click();
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}

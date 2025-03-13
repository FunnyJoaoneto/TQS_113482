package org.deti.ua.pt;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReservesPage {

    private WebDriver driver;

    @FindBy(className = "table")
    private WebElement tableOfFlights;

    public ReservesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectFlight(int number) {
        // Locate all buttons with "Choose This Flight"
        List<WebElement> flightButtons = tableOfFlights.findElements(By.xpath("//input[@value='Choose This Flight']"));

        // Ensure the index is within bounds
        if (number > 0 && number <= flightButtons.size()) {
            flightButtons.get(number - 1).click();
        } else {
            throw new IndexOutOfBoundsException("Invalid flight selection: " + number);
        }
    }
}

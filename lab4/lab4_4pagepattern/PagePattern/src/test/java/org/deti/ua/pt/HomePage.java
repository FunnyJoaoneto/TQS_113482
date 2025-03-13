package org.deti.ua.pt;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private WebDriver driver;
    private final String url = "https://blazedemo.com/";

    @FindBy(name = "fromPort")
    private WebElement fromPort;

    @FindBy(name = "toPort")
    private WebElement toPort;

    @FindBy(css = "input[value=\'Find Flights\']")
    private WebElement findFlights;

    public String getUrl() {
        return url;
    }

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public void setFromPort(String from) {
        fromPort.findElement(By.cssSelector("[value=\'" + from + "\']")).click();
    }

    public void setToPort(String to) {
        toPort.findElement(By.cssSelector("[value=\'" + to + "\']")).click();
    }

    public void clickOnFindFlights() {
        findFlights.click();
        // return new ReservesPage(this.driver);
    }
}

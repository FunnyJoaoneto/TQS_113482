package org.deti.ua.pt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import static io.github.bonigarcia.seljup.BrowserType.CHROME;
import io.github.bonigarcia.seljup.DockerBrowser;
import io.github.bonigarcia.seljup.SeleniumJupiter;

@ExtendWith(SeleniumJupiter.class)
public class BlazeDemoTest {

    @Test
    public void test(@DockerBrowser(type = CHROME) WebDriver driver) {
        HomePage HomePage = new HomePage(driver);
        HomePage.setFromPort("Paris");
        HomePage.setToPort("Berlin");

        HomePage.clickOnFindFlights();
        ReservesPage reserves = new ReservesPage(driver);
        assertThat(driver.getTitle()).isEqualTo("BlazeDemo - reserve");

        reserves.selectFlight(1);
        PurchasePage purchase = new PurchasePage(driver);
        assertThat(driver.getTitle()).isEqualTo("BlazeDemo Purchase");
        purchase.setInputName("First Last");
        purchase.setAddress("Rua rua rua");
        purchase.setCity("Anytown");
        purchase.setState("State");
        purchase.setZipCode("12345");
        purchase.setCardType("Visa");
        purchase.setCreditCardNumber("1111222233334444");
        purchase.setCreditCardMonth("12");
        purchase.setCreditCardYear("2020");
        purchase.setNameOnCard("Jonh Smith");
        purchase.toggleRememberMe();
        purchase.clickOnPurchaseFlight();

        assertThat(driver.getTitle()).isEqualTo("BlazeDemo Confirmation");
        assertThat(driver.getCurrentUrl()).isEqualTo("https://blazedemo.com/confirmation.php");
    }
}

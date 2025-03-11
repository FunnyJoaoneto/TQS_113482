package org.tqs.deti.ua;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Dimension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll and @AfterAll can use non-static methods
public class SelectBuyTripTest {
  private WebDriver driver;

  @BeforeAll
  void setUp() {
    driver = new FirefoxDriver(); // Manually create WebDriver
  }

  @AfterAll
  void tearDown() {
    driver.quit(); // Close WebDriver
  }

  @Test
  void selectBuyTrip() {
    driver.get("https://blazedemo.com/");
    driver.manage().window().setSize(new Dimension(929, 1053));
    assertThat(driver.getTitle()).isEqualTo("BlazeDemo");

    driver.findElement(By.name("fromPort")).click();
    {
      WebElement dropdown = driver.findElement(By.name("fromPort"));
      dropdown.findElement(By.xpath("//option[. = 'Boston']")).click();
    }
    driver.findElement(By.cssSelector(".form-inline:nth-child(1) > option:nth-child(3)")).click();

    driver.findElement(By.name("toPort")).click();
    {
      WebElement dropdown = driver.findElement(By.name("toPort"));
      dropdown.findElement(By.xpath("//option[. = 'Dublin']")).click();
    }
    driver.findElement(By.cssSelector(".form-inline:nth-child(4) > option:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".btn-primary")).click();

    assertThat(driver.getTitle()).isEqualTo("BlazeDemo - reserve");
    driver.findElement(By.cssSelector("tr:nth-child(5) .btn")).click();

    driver.findElement(By.id("inputName")).sendKeys("First Last");
    driver.findElement(By.id("address")).sendKeys("123 Main Lt");
    driver.findElement(By.id("city")).sendKeys("Anytown");
    driver.findElement(By.id("state")).sendKeys("State");
    driver.findElement(By.id("zipCode")).sendKeys("12345");
    driver.findElement(By.id("creditCardNumber")).sendKeys("1111222233334444");
    driver.findElement(By.id("nameOnCard")).sendKeys("Jonh Smith");
    driver.findElement(By.id("rememberMe")).click();
    driver.findElement(By.cssSelector(".btn-primary")).click();

    assertThat(driver.getTitle()).isEqualTo("BlazeDemo Confirmation");
  }
}

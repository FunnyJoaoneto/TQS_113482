package org.deti.ua.pt;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class PurchasePage {
    private WebDriver driver;

    @FindBy(id = "inputName")
    private WebElement inputName;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "zipCode")
    private WebElement zipCode;

    @FindBy(id = "cardType")
    private WebElement cardType;

    @FindBy(id = "creditCardNumber")
    private WebElement creditCardNumber;

    @FindBy(id = "creditCardMonth")
    private WebElement creditCardMonth;

    @FindBy(id = "creditCardYear")
    private WebElement creditCardYear;

    @FindBy(id = "nameOnCard")
    private WebElement nameOnCard;

    @FindBy(id = "rememberMe")
    private WebElement rememberMe;

    @FindBy(css = "input[value='Purchase Flight']")
    private WebElement purchaseFlight;

    public PurchasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setInputName(String name) {
        inputName.sendKeys(name);
    }

    public void setAddress(String addr) {
        address.sendKeys(addr);
    }

    public void setCity(String cty) {
        city.sendKeys(cty);
    }

    public void setState(String st) {
        state.sendKeys(st);
    }

    public void setZipCode(String zip) {
        zipCode.sendKeys(zip);
    }

    public void setCardType(String type) {
        Select cardTypeSelect = new Select(cardType);
        cardTypeSelect.selectByVisibleText(type);
    }

    public void setCreditCardNumber(String number) {
        creditCardNumber.sendKeys(number);
    }

    public void setCreditCardMonth(String month) {
        creditCardMonth.sendKeys(month);
    }

    public void setCreditCardYear(String year) {
        creditCardYear.sendKeys(year);
    }

    public void setNameOnCard(String name) {
        nameOnCard.sendKeys(name);
    }

    public void toggleRememberMe() {
        rememberMe.click();
    }

    public void clickOnPurchaseFlight() {
        purchaseFlight.click();
    }
}

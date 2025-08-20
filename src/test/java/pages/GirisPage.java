package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ReasubleMethods;

public class GirisPage {
    public GirisPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy (xpath = "//a[@href='https://giris.turkiye.gov.tr/Giris/gir']")
    public WebElement girisButon;

    @FindBy (xpath = "//input[@name='tridField']")
    public WebElement tcNo;

    @FindBy (xpath = "//input[@name='egpField']")
    public WebElement sifre;

    @FindBy (xpath = "//button[@name='submitButton']")
    public WebElement submitButon;

    public void login() {
        ReasubleMethods.clickableWait(Driver.getDriver(), girisButon, 15);
        girisButon.click();

        ReasubleMethods.visibleWait(Driver.getDriver(), tcNo, 15)
                .sendKeys(ConfigReader.getProperty("kullanici_adi"));

        sifre.sendKeys(ConfigReader.getProperty("sifre"));

        submitButon.click();
    }
}

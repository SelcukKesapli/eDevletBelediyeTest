package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class ArsaPage {
    public ArsaPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//select[@name='mahalle']")
    public WebElement mahalleSec;

    @FindBy(xpath = "//select[@name='caddesokak']")
    public WebElement caddeSec;

    @FindBy(xpath = "//select[@name='yil']")
    public WebElement yilSec;

    @FindBy (xpath = "//input[@class='submitButton']")
    public WebElement sorgulaButon;

    @FindBy (xpath = "//div[@id='mahalle-error']")
    public WebElement hataMesaj;
}
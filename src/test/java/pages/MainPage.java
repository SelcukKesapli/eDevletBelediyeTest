package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class MainPage {
    public MainPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//input[@id='searchField']")
    public WebElement aramaKutusu;

    @FindBy(xpath = "//a[@class='agency']")
    public WebElement golbasiSec;

    @FindBy(xpath = "//a[@href='/ankara-golbasi-belediyesi?belediye=TumHizmetler']")
    public WebElement tumHizmetlerButon;
}

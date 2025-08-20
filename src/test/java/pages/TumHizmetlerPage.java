package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class TumHizmetlerPage {
    public TumHizmetlerPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[@href='golbasi-belediyesi-arsa-rayic']")
    public WebElement arsaButon;

    @FindBy(xpath = "//a[@href='golbasi-belediyesi-beyan-sorgulama']")
    public WebElement beyanButon;

    @FindBy(xpath = "//a[@href='golbasi-belediyesi-sicil-sorgulama']")
    public WebElement sicilButon;

    @FindBy(xpath = "//a[@href='golbasi-belediyesi-borc-sorgulama']")
    public WebElement tahakkukButon;

    @FindBy(xpath = "//a[@href='golbasi-belediyesi-tahsilat-sorgulama']")
    public WebElement tahsilatButon;

}

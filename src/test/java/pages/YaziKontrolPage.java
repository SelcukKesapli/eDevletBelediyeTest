package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class YaziKontrolPage {
    public YaziKontrolPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy (xpath = "//span[normalize-space()='Herhangi bir kayıt bulunamadı.']")
    public WebElement beyanYazi;

    @FindBy (xpath = "//span[normalize-space()='Herhangi bir kayıt bulunamadı.']")
    public WebElement sicilYazi;

    @FindBy (xpath = "//div[@class='warningContainer']")
    public WebElement tahakkukYazi;

    @FindBy (xpath = "//span[normalize-space()='Herhangi bir kayıt bulunamadı.']")
    public WebElement tahsilatYazi;
}

package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class ArsaRayicPage {
    public ArsaRayicPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy (xpath = "//caption[normalize-space()='Arsa Rayiç Değer Bilgisi']")
    public WebElement arsaRayicBaslik;

    @FindBy (xpath = "//table//th[normalize-space()='Mahalle Adı']")
    public WebElement mahalleAdi;

    @FindBy (xpath = "//table//th[normalize-space()='Cadde/Sokak Adı']")
    public WebElement caddeSokakAdi;

    @FindBy (xpath = "//table//th[normalize-space()='Yıl']")
    public WebElement yil;

    @FindBy (xpath = "//table//th[normalize-space()='Rayiç Değeri (TL)']")
    public WebElement rayicDegeri;
}

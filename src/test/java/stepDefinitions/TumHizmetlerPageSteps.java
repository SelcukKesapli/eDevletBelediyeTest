package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import logpackages.Log;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.*;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ReasubleMethods;
import utilities.ScenarioFail;

import java.util.List;

public class TumHizmetlerPageSteps {
    MainPage mainPage =new MainPage();
    TumHizmetlerPage tumHizmetlerPage =new TumHizmetlerPage();
    ArsaPage arsaPage =new ArsaPage();
    ArsaRayicPage arsaRayicPage =new ArsaRayicPage();
    YaziKontrolPage yaziKontrolPage =new YaziKontrolPage();


    @Given("e devlete ana ekrana gidilir")
    public void e_devlete_ana_ekrana_gidilir() {
        Log.info("ekrana gidilir");
        Driver.getDriver().get(ConfigReader.getProperty("base_url"));

        String expectedLink = ConfigReader.getProperty("base_url");
        String actualLink = Driver.getDriver().getCurrentUrl();

        try{
            Assert.assertEquals(expectedLink, actualLink);
            Log.info("ekran açıldı");
        }catch(AssertionError ae){
            Log.error("e devlet ana ekrana gidilemedi!!"+ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(),Status.FAILED);
        }catch (Exception e){
            Log.error("ekran ana ekrana gidilemedi!!"+e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(),Status.FAILED);
        }
    }

    @And("arama cubuguna golbasi yazilir")
    public void arama_cubuguna_golbasi_yazilir(){
        Log.info("arama çubuğu bekleniyor");

        try{
            WebElement input =ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.aramaKutusu,15);
            input.clear();
            input.sendKeys("Gölbaşı");

            String expected = "Gölbaşı";
            String actual = input.getAttribute("value");
            Assert.assertEquals(expected, actual);

            Log.info("Arama çubuğu açıldı ve gölbaşı aratıldı");
            Allure.step("Arama çubuğu açıldı ve gölbaşı aratıldı",Status.PASSED);
        }catch(AssertionError ae){
            String msg = "Arama kutusu doğrulaması başarısız: " + ae.getMessage();
            Log.error(msg);
            ScenarioFail.mark(msg);
            Allure.step(msg,Status.FAILED);
        }catch (Exception e){
            String msg = "Beklenmeyen bir hata oluştu " + e.getMessage();
            Log.error(msg);
            ScenarioFail.mark(msg);
            Allure.step(msg,Status.FAILED);
        }
    }

    @And("golbasi belediyesi sayfasina gidilir")
    public void golbasi_belediyesi_sayfasaina_gidilir() {
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(), mainPage.golbasiSec, 15).click();
            String actualLink = Driver.getDriver().getCurrentUrl();
            String expectedLink = "https://www.turkiye.gov.tr/ankara-golbasi-belediyesi";
            Assert.assertEquals(expectedLink, actualLink);
            Log.info("golbasi sayfasina gidildi");
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("tum hizmetler sekmesine gidilir")
    public void tum_hizmetler_sekmesine_gidilir(){
        ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.tumHizmetlerButon,15).click();
        String expected = "https://www.turkiye.gov.tr/ankara-golbasi-belediyesi?belediye=TumHizmetler";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(),expected,10);
        String actual = Driver.getDriver().getCurrentUrl();

        try{
            Assert.assertEquals(expected, actual);
            Log.info("tum hizmetler sekmesine gidildi");
            Allure.step("tum hizmetler sekmesine gidildi",Status.PASSED);
        }catch(AssertionError | Exception ex){
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Given("arsa metrekare ekranina girilir")
    public void arsa_metrekare_ekranina_girilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15).click();

        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(), expected, 10);
        String actual = Driver.getDriver().getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("arsa metrekare ekranina gidildi");
            Allure.step("arsa metrekare ekranina gidildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    List<String> mahalleList;

    @Given("mahalle verileri cekilir")
    public void mahalleVerileriCekilir() {
        // verileri çek
        mahalleList = ReasubleMethods.dropdownToList(arsaPage.mahalleSec);

        boolean expected = true;
        boolean actual   =! mahalleList.isEmpty();

        try{
            Assert.assertEquals("Dropdown verileri çekilemedi!", expected, actual);
            Log.info("Veriler çekildi. Toplam seçenek: " + mahalleList.size());
            Allure.step("Veriler çekildi (" + mahalleList.size() + " seçenek)", Status.PASSED);
        }catch(AssertionError | Exception ex){
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("alfabetik mi ve tekrar eden var mi dogrulanir")
    public void alfabetik_mi_ve_tekrar_eden_var_mi_dogrulanir() {
        Log.info("Mahalle isimleri alfabetik/tekrarsiz mi kontrol ediliyor");

        boolean alfabetikMi    = ReasubleMethods.alfabetikMi(mahalleList);
        boolean tekrarVarMi = ReasubleMethods.tekrarEdiyorMu(mahalleList);

        boolean expected = true;
        boolean actual   = (alfabetikMi && !tekrarVarMi);

        if (!alfabetikMi)
            Allure.step("Sıralama bozuk ", Status.FAILED);
        if (tekrarVarMi)
            Allure.step("Tekrar eden öğe(ler) var ", Status.FAILED);

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Mahalle listesi alfabetik ve tekrarsiz");
            Allure.step("Mahalle listesi alfabetik ve tekrarsiz", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Given("arsa metrekare ekranına gidilir")
    public void arsa_metrekare_ekranina_gidilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15).click();

        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(), expected, 10);
        String actual = Driver.getDriver().getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("arsa metrekare ekranina gidildi");
            Allure.step("arsa metrekare ekranina gidildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Given("ahiboz mahallesi secilir")
    public void ahiboz_mahallesi_secilir() {
        Log.info("Ahiboz Mahallesi aranıyor");
        ReasubleMethods.selectByVisibleText(arsaPage.mahalleSec, "AHİBOZ MAHALLESİ");

        String expected = "AHİBOZ MAHALLESİ";
        String actual = arsaPage.mahalleSec
                .findElement(By.cssSelector("option:checked"))
                .getText().trim();

        try {
            org.junit.Assert.assertEquals(expected, actual);
            Log.info("Ahiboz Mahallesi seçildi");
            io.qameta.allure.Allure.step("Ahiboz Mahallesi seçildi",Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            io.qameta.allure.Allure.step(ex.getMessage(),Status.FAILED);
        }
    }

    List<String> caddeList;

    @Given("cadde verileri cekilir")
    public void caddeVerileriCekilir() {
        caddeList = ReasubleMethods.dropdownToList(arsaPage.caddeSec);

        boolean expected = true;
        boolean actual   =! caddeList.isEmpty();

        try{
            Assert.assertEquals(expected, actual);
            Log.info("Cadde verileri çekildi. Toplam seçenek: " + caddeList.size());
            Allure.step("Cadde verileri çekildi", Status.PASSED);
        }catch(AssertionError | Exception ex){
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("alfabetik mi ve tekrar eden varmi dogrulanir")
    public void alfabetik_mi_ve_tekrar_eden_varmi_dogrulanir() {
        Log.info("Cadde listesi alfabetik mi ve tekrar eden var mi kontrol ediliyor");

        boolean alfabetikMi    = ReasubleMethods.alfabetikMi(caddeList);
        boolean tekrarEdiyorMu = ReasubleMethods.tekrarEdiyorMu(caddeList);

        boolean expected = true;
        boolean actual   = (alfabetikMi && !tekrarEdiyorMu);

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Cadde listesi kriterleri saglandi");
            Allure.step("Cadde listesi alfabetik ve tekrarsiz", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }


    @Given("arsa metrekare alanina gidilir")
    public void arsaMetrekareAlaninaGidilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15).click();

        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(), expected, 10);
        String actual = Driver.getDriver().getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("arsa metrekare ekranina gidildi");
            Allure.step("arsa metrekare ekranina gidildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    List<String> yilList;

    @And("yil listesinin verileri cekilir")
    public void yilListesininVerileriCekilir() {
        yilList = ReasubleMethods.dropdownToList(arsaPage.yilSec);

        boolean expected = true;
        boolean actual   = !yilList.isEmpty();

        try {
            Assert.assertEquals("Yil listesi cekilemedi!", expected, actual);
            Log.info("Yil verileri cekildi. Toplam secenek: " + yilList.size());
            Allure.step("Yil verileri cekildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("buyukten kucuge mi siralanmis dogrulanir")
    public void buyuktenKucugeMiSiralanmisDogrulanir() {
        Log.info("Yil listesi sirali mi ve tekrar eden var mi kontrol ediliyor");

        boolean siraliMi       = ReasubleMethods.buyuktenKucugeMi(yilList);
        boolean tekrarEdiyorMu = ReasubleMethods.tekrarEdiyorMu(yilList);

        boolean expected = true;
        boolean actual   = (siraliMi && !tekrarEdiyorMu);

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Yil listesi dogrulandi (buyukten kucuge & tekrarsiz)");
            Allure.step("Yil listesi buyukten kucuge ve tekrarsiz", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Given("arsa metrekare alanına gidilir")
    public void arsaMetrekareAlanina_Gidilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15).click();

        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(), expected, 10);
        String actual = Driver.getDriver().getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("arsa metrekare ekranina gidildi");
            Allure.step("arsa metrekare ekranina gidildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @And("sorgula butonuna basilir")
    public void sorgulaButonunaBasilir() {
        WebElement btn = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.sorgulaButon, 15);
        btn.click();

        boolean expected = true;
        boolean actual   = ReasubleMethods.waitClickedOnce(Driver.getDriver(), btn, 15);

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Sorgula butonuna tek tik dogrulandi");
            Allure.step("Sorgula butonuna tek tik dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("gerekli hata mesaji cikiyor mu dogrulanir")
    public void gerekliHataMesajiCikiyorMuDogrulanir() {
        Log.info("Gerekli hata mesaji var mi kontrol ediliyor");

        String expectedMessage = "Bu alanın doldurulması zorunludur.";
        String actualMessage   = ReasubleMethods.elementIcindekiText(arsaPage.hataMesaj).trim();

        try {
            Assert.assertEquals(expectedMessage, actualMessage);
            Log.info("Hata mesaji kontrol edildi");
            Allure.step("Hata mesaji dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Given("arsa metrakare ekranina girilir")
    public void arsaMetrakareEkraninaGirilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15).click();

        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic";
        ReasubleMethods.waitUrlToBe(Driver.getDriver(), expected, 10);
        String actual = Driver.getDriver().getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("arsa metrekare ekranina gidildi");
            Allure.step("arsa metrekare ekranina gidildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @When("eymir mahallesi secilir")
    public void eymirMahallesiSecilir() {
        Log.info("eymir mahallesi seciliyor");
        ReasubleMethods.selectByVisibleText(arsaPage.mahalleSec, "EYMİR MAHALLESİ");

        String expected = "EYMİR MAHALLESİ";
        String actual   = arsaPage.mahalleSec
                .findElement(By.cssSelector("option:checked"))
                .getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Eymir mahallesi secildi");
            Allure.step("Eymir mahallesi secildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @And("3803. cadde secilir")
    public void sokakSecilir() {
        Log.info("3803. cadde aranıyor");
        ReasubleMethods.clickableWait(Driver.getDriver(), arsaPage.caddeSec, 20);
        ReasubleMethods.waitAndSelectByText(Driver.getDriver(), arsaPage.caddeSec, "3803. CADDE", 15);

        String expected = "3803. CADDE";
        String actual   = arsaPage.caddeSec
                .findElement(By.cssSelector("option:checked"))
                .getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("3803. cadde seçildi");
            Allure.step("3803. cadde seçildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @And("2025 yili secilir")
    public void yiliSecilir() {
        ReasubleMethods.clickableWait(Driver.getDriver(), arsaPage.yilSec, 20);
        ReasubleMethods.selectByVisibleText(arsaPage.yilSec, "2025");

        String expected = "2025";
        String actual   = arsaPage.yilSec
                .findElement(By.cssSelector("option:checked"))
                .getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("2025 yili secildi");
            Allure.step("2025 yili secildi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @And("sorgula butonuna tiklanir")
    public void sorgulaButonunaTiklanir() {
        WebDriver d = Driver.getDriver();

        // 1) Butonu bekle ve tıkla
        ReasubleMethods.visibleWait(d, arsaPage.sorgulaButon, 15).click();

        // 2) Hedef URL'i bekle ve doğrula
        String expected = "https://www.turkiye.gov.tr/golbasi-belediyesi-arsa-rayic?asama=1";
        ReasubleMethods.waitUrlToBe(d, expected, 20);
        String actual = d.getCurrentUrl();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Sorgula sonrası beklenen sayfaya gidildi: " + actual);
            Allure.step("Sorgula -> URL doğrulandı", io.qameta.allure.model.Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error("URL doğrulanamadı. expected=" + expected + " actual=" + actual);
            ScenarioFail.mark(ex.getMessage());
            Allure.step("URL hatası: expected=" + expected + " actual=" + actual, io.qameta.allure.model.Status.FAILED);
        }
    }

    @Then("basliklar cikiyor mu dogrulanir")
    public void basliklarCikiyorMuDogrulanir() {
        Log.info("Basliklar cikiyor mu dogrulaniyor");
        ReasubleMethods.visibleWait(Driver.getDriver(), arsaRayicPage.arsaRayicBaslik, 15);

        String expBaslik  = "Arsa Rayiç Değer Bilgisi";
        String expMahalle = "Mahalle Adı";
        String expCadde   = "Cadde/Sokak Adı";
        String expYil     = "Yıl";
        String expRayic   = "Rayiç Değeri (TL)";

        String actBaslik  = arsaRayicPage.arsaRayicBaslik.getText().trim();
        String actMahalle = arsaRayicPage.mahalleAdi.getText().trim();
        String actCadde   = arsaRayicPage.caddeSokakAdi.getText().trim();
        String actYil     = arsaRayicPage.yil.getText().trim();
        String actRayic   = arsaRayicPage.rayicDegeri.getText().trim();

        try {
            Assert.assertEquals(expBaslik,  actBaslik);
            Assert.assertEquals(expMahalle, actMahalle);
            Assert.assertEquals(expCadde,   actCadde);
            Assert.assertEquals(expYil,     actYil);
            Assert.assertEquals(expRayic,   actRayic);

            Log.info("Basliklar dogrulandi");
            Allure.step("Basliklar dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("beyan bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void beyanBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Beyan bilgi ekrani kontrol ediliyor");

        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.beyanButon, 15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(), yaziKontrolPage.beyanYazi, 15);

        String expected = "Herhangi bir kayıt bulunamadı.";
        String actual   = yaziKontrolPage.beyanYazi.getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Beyan bilgi ekrani kontrol edildi");
            Allure.step("Beyan bilgi ekrani yazisi dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("sicil bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void sicilBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Sicil bilgi ekrani kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.sicilButon, 15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(), yaziKontrolPage.sicilYazi, 15);

        String expected = "Herhangi bir kayıt bulunamadı.";
        String actual   = yaziKontrolPage.sicilYazi.getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Sicil bilgi ekrani kontrol edildi");
            Allure.step("Sicil bilgi ekrani yazisi dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("tahakkuk bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahakkukBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahakkuk bilgi ekrani yazi kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.tahakkukButon, 15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(), yaziKontrolPage.tahakkukYazi, 15);

        String expected = "Sicil Kodu Bulunamadı.";
        String actual   = yaziKontrolPage.tahakkukYazi.getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Tahakkuk bilgi ekrani kontrol edildi");
            Allure.step("Tahakkuk bilgi ekrani yazisi dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }

    @Then("tahsilat bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahsilatBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahsilat bilgi ekrani kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.tahsilatButon, 15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(), yaziKontrolPage.tahsilatYazi, 15);

        String expected = "Herhangi bir kayıt bulunamadı.";
        String actual   = yaziKontrolPage.tahsilatYazi.getText().trim();

        try {
            Assert.assertEquals(expected, actual);
            Log.info("Tahsilat bilgi ekrani kontrol edildi");
            Allure.step("Tahsilat bilgi ekrani yazisi dogrulandi", Status.PASSED);
        } catch (AssertionError | Exception ex) {
            Log.error(ex.getMessage());
            ScenarioFail.mark(ex.getMessage());
            Allure.step(ex.getMessage(), Status.FAILED);
        }
    }
}
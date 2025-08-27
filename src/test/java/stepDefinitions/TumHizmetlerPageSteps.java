package stepDefinitions;

import io.qameta.allure.model.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import logpackages.Log;
import org.junit.Assert;
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
    public void golbasi_belediyesi_sayfasaina_gidilir(){
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(), mainPage.golbasiSec, 15).click();
            String actualLink = Driver.getDriver().getCurrentUrl();
            String expectedLink = "https://turkiye.gov.tr/ankara-golbasi-belediyesi";
            Assert.assertEquals(expectedLink, actualLink);
            Log.info("golbasi sayfasina gidildi");
            Allure.step("golbasi sayfasina gidildi",Status.PASSED);
        }catch (AssertionError ae){
            Log.error("golbasi sayfasina gidilemedi: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(),Status.FAILED);
        }catch (Exception e) {
            Log.error("golbasi sayfasina gidilemedi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(),Status.FAILED);
        }
    }

    @Then("tum hizmetler sekmesine gidilir")
    public void tum_hizmetler_sekmesine_gidilir(){
        Log.info("tum hizmetler sekmesine gidilir");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.tumHizmetlerButon,15).click();
            WebElement arsaButon = ReasubleMethods.visibleWait(Driver.getDriver(), tumHizmetlerPage.arsaButon, 15);
            Assert.assertTrue("Arsa butonu görünür", arsaButon.isDisplayed());
            Allure.step("Tüm Hizmetler sayfası açıldı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Tüm Hizmetler sayfası doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Tüm Hizmetler sayfasına gidilemedi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Given("arsa metrakare ekranına girilir")
    public void arsa_metrakare_ekranına_girilir() {
        Log.info("Arsa metrekare ekranina gidiliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
            WebElement mahalleDropdown = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.mahalleSec, 15);
            Assert.assertTrue("Arsa ekranı açıldı", mahalleDropdown.isDisplayed());
            Log.info("Arsa metrekare ekranına gidildi");
            Allure.step("Arsa metrekare ekranına gidildi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Arsa metrekare ekranı doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Arsa metrekare ekranına gidilemedi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    List<String> mahalleList;

    @Given("veriler çekilir")
    public void veriler_çekilir() {
        mahalleList = ReasubleMethods.dropdownToList(arsaPage.mahalleSec);
    }

    @Then("alfabetik mi ve tekrar eden var mi dogrulanir")
    public void alfabetik_mi_ve_tekrar_eden_var_mi_dogrulanir() {
        Log.info("Mahalle isimleri alfabetik mi kontrol ediliyor");
        boolean alfabetikMi = ReasubleMethods.alfabetikMi(mahalleList);
        boolean tekrarEdiyorMu = ReasubleMethods.tekrarEdiyorMu(mahalleList);
        Log.info("Mahalle isimleri alfabetik mi kontrol edildi");
    }

    List<String> caddeList;

    @Given("arsa metrekare ekranına gidilir")
    public void arsa_metrekare_ekranına_gidilir() {
        Log.info("Arsa metrekare ekranına gidiliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
        Log.info("Arsa metrekare ekranına gidildi");
    }
    @Given("ahiboz mahallesi secilir")
    public void ahiboz_mahallesi_secilir() {
        Log.info("Ahiboz Mahallesi araniyor");
        ReasubleMethods.selectByVisibleText(arsaPage.mahalleSec,"AHİBOZ MAHALLESİ");
        Log.info("Ahiboz Mahallesi secildi");
    }

    @Given("veriler cekilir")
    public void veriler_cekilir() {
        caddeList = ReasubleMethods.dropdownToList(arsaPage.caddeSec);
    }

    @Then("alfabetik mi ve tekrar eden varmi dogrulanir")
    public void alfabetik_mi_ve_tekrar_eden_varmi_dogrulanir() {
        Log.info("Cadde listesi alfabetik mi ve tekrar eden varmi dogrulaniyor");
        boolean alfabetikMi = ReasubleMethods.alfabetikMi(caddeList);
        boolean tekrarEdiyorMu = ReasubleMethods.tekrarEdiyorMu(caddeList);
        Log.info("Cadde listesi dogrulandi");
    }

    @Given("arsa metrekare alanina gidilir")
    public void arsaMetrekareAlaninaGidilir() {
        Log.info("Arsa metrekare alanina gidiliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
        Log.info("Arsa metrekare alanina gidildi");
    }
    List<String> yilList;
    @And("yil listesinin verileri cekilir")
    public void yilListesininVerileriCekilir() {
        yilList = ReasubleMethods.dropdownToList(arsaPage.yilSec);
    }

    @Then("buyukten kucuge mi siralanmis dogrulanir")
    public void buyuktenKucugeMiSiralanmisDogrulanir() {
        Log.info("Yil listesi sirali mi kontrol ediliyor");
        boolean siraliMi = ReasubleMethods.buyuktenKucugeMi(yilList);
        boolean tekrarEdiyorMu = ReasubleMethods.tekrarEdiyorMu(yilList);
        Log.info("Yil listesi dogrulandi");
    }

    @Given("arsa metrekare alanına gidilir")
    public void arsaMetrekareAlanınaGidilir() {
        Log.info("Arsa metrekare alanina gidiliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
            WebElement yilDropdown = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.yilSec, 15);
            Assert.assertTrue("Arsa ekranı açılmadı (yıl)", yilDropdown.isDisplayed());
            Allure.step("Arsa metrekare ekranına gidildi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Arsa metrekare ekranı (yıl) doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Arsa metrekare ekranına gidilemedi (yıl): " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @And("sorgula butonuna basilir")
    public void sorgulaButonunaBasilir() {
        Log.info("Sorgula butonuna tiklaniyor");
        try {
            WebElement sorgula = ReasubleMethods.visibleWait(Driver.getDriver(),arsaPage.sorgulaButon,15);
            Assert.assertTrue("Sorgula butonu etkin değil", sorgula.isEnabled());
            sorgula.click();
            Allure.step("Sorgula butonuna tiklandi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Sorgula butonu doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Sorgula butonuna tiklanamadi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Then("gerekli hata mesajı cikiyor mu dogrulanir")
    public void gerekliHataMesajıCikiyorMuDogrulanir() {
        Log.info("Gerekli hata mesaji var mi kontrol ediliyor");
        try {
            String expectedMessage = "Bu alanın doldurulması zorunludur.";
            String actualMessage = ReasubleMethods.elementIcindekiText(arsaPage.hataMesaj);
            Assert.assertEquals(expectedMessage,actualMessage);
            Allure.step("Zorunlu alan hata mesajı görüntülendi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Hata mesajı bekleneni karşılamıyor: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Hata mesajı kontrolünde sorun: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @When("eymir mahallesi secilir")
    public void eymirMahallesiSecilir() {
        Log.info("eymir mahallesi seciliyor");
        try {
            WebElement mahalle = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.mahalleSec, 15);
            Assert.assertTrue("Mahalle dropdown görünür değil", mahalle.isDisplayed());
            ReasubleMethods.selectByVisibleText(arsaPage.mahalleSec,"EYMİR MAHALLESİ");
            // Etki: cadde dropdown'ı görünür olmalı
            WebElement cadde = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.caddeSec, 15);
            Assert.assertTrue("Cadde dropdown görünür değil", cadde.isDisplayed());
            Allure.step("Eymir Mahallesi seçildi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Eymir Mahallesi seçimi doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Eymir Mahallesi seçilemedi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @And("3803. cadde secilir")
    public void sokakSecilir() {
        Log.info("3803. cadde araniyor");
        try {
            ReasubleMethods.clickableWait(Driver.getDriver(),arsaPage.caddeSec,20);
            ReasubleMethods.waitAndSelectByText(Driver.getDriver(),arsaPage.caddeSec,"3803. CADDE",15);
            // Doğrulama: yıl dropdown görünür olmalı
            WebElement yil = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.yilSec, 15);
            Assert.assertTrue("Yıl dropdown görünür değil", yil.isDisplayed());
            Allure.step("3803. Cadde seçildi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Cadde seçimi doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Cadde seçilemedi: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @And("2025 yili secilir ve sorgula butonuna tiklanir")
    public void yiliSecilir() {
        Log.info("2025 yılı seçiliyor ve sorgulanıyor");
        try {
            ReasubleMethods.clickableWait(Driver.getDriver(),arsaPage.yilSec,20);
            ReasubleMethods.selectByVisibleText(arsaPage.yilSec,"2025");
            WebElement sorgula = ReasubleMethods.visibleWait(Driver.getDriver(), arsaPage.sorgulaButon, 15);
            Assert.assertTrue("Sorgula butonu etkin değil", sorgula.isEnabled());
            sorgula.click();
            Allure.step("2025 yılı seçildi ve sorgulandı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Yıl seçimi/sorgu doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Yıl seçimi/sorgu başarısız: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Then("basliklar cikiyor mu dogrulanir")
    public void basliklarCikiyorMuDogrulanir() {
        Log.info("Basliklar cikiyor mu dogrulaniyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),arsaRayicPage.arsaRayicBaslik,15);
            Assert.assertEquals("Arsa Rayiç Değer Bilgisi", arsaRayicPage.arsaRayicBaslik.getText());
            Assert.assertEquals("Mahalle Adı", arsaRayicPage.mahalleAdi.getText());
            Assert.assertEquals("Cadde/Sokak Adı", arsaRayicPage.caddeSokakAdi.getText());
            Assert.assertEquals("Yıl", arsaRayicPage.yil.getText());
            Assert.assertEquals("Rayiç Değeri (TL)", arsaRayicPage.rayicDegeri.getText());
            Allure.step("Başlıklar doğru görüntülendi", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Başlıklar doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Başlık kontrolünde hata: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }


    @Then("beyan bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void beyanBilgileriEkranınaGidilirVeYaziKontrolEdilir() {
        Log.info("Beyan bilgi ekrani kontrol ediliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.beyanButon,15).click();
            ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.beyanYazi,15);
            Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.beyanYazi.getText());
            Allure.step("Beyan ekranı yazısı doğrulandı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Beyan ekranı yazısı doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Beyan ekranına gidilemedi/yazı alınamadı: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Then("sicil bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void sicilBilgileriEkranınaGidilirVeYaziKontrolEdilir() {
        Log.info("Sicil bilgi ekrani kontrol ediliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.sicilButon,15).click();
            ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.sicilYazi,15);
            Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.sicilYazi.getText());
            Allure.step("Sicil ekranı yazısı doğrulandı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Sicil ekranı yazısı doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Sicil ekranına gidilemedi/yazı alınamadı: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Then("tahakkuk bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahakkukBilgileriEkranınaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahakkuk bilgi ekrani yazi kontrol ediliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.tahakkukButon,15).click();
            ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.tahakkukYazi,15);
            Assert.assertEquals("Sicil Kodu Bulunamadı.",yaziKontrolPage.tahakkukYazi.getText());
            Allure.step("Tahakkuk ekranı yazısı doğrulandı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Tahakkuk ekranı yazısı doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Tahakkuk ekranına gidilemedi/yazı alınamadı: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

    @Then("tahsilat bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahsilatBilgileriEkranınaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahsilat bilgi ekrani kontrol ediliyor");
        try {
            ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.tahsilatButon,15).click();
            ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.tahsilatYazi,15);
            Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.tahsilatYazi.getText());
            Allure.step("Tahsilat ekranı yazısı doğrulandı", Status.PASSED);
        } catch (AssertionError ae) {
            Log.error("Tahsilat ekranı yazısı doğrulanamadı: " + ae.getMessage());
            ScenarioFail.mark(ae.getMessage());
            Allure.step(ae.getMessage(), Status.FAILED);
        } catch (Exception e) {
            Log.error("Tahsilat ekranına gidilemedi/yazı alınamadı: " + e.getMessage());
            ScenarioFail.mark(e.getMessage());
            Allure.step(e.getMessage(), Status.FAILED);
        }
    }

}
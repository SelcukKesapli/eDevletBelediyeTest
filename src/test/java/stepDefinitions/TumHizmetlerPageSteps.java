package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import logpackages.Log;
import org.junit.Assert;
import pages.*;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ReasubleMethods;

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
        Log.info("ekran açıldı");
    }

    @And("arama cubuguna golbasi yazilir")
    public void arama_cubuguna_golbasi_yazilir(){
        Log.info("arama çubuğu bekleniyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.aramaKutusu,15)
                .sendKeys("Gölbaşı");
        Log.info("Arama çubuğu açıldı ve gölbaşı aratıldı");
    }
    @And("golbasi belediyesi sayfasina gidilir")
    public void golbasi_belediyesi_sayfasina_gidilir(){
        ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.golbasiSec,15).click();
        Log.info("golbasi sayfasina gidilir");
    }
    @Then("tum hizmetler sekmesine gidilir")
    public void tum_hizmetler_sekmesine_gidilir(){
        ReasubleMethods.visibleWait(Driver.getDriver(),mainPage.tumHizmetlerButon,15).click();
        Log.info("tum hizmetler sekmesine gidilir");
    }

    @Given("arsa metrakare ekranına girilir")
    public void arsa_metrakare_ekranina_girilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
        Log.info("Arsa metrekare ekranina gidildi");
    }

    List<String> mahalleList;

    @Given("veriler çekilir")
    public void verilerCekilir() {
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
    public void arsa_metrekare_ekranina_gidilir() {
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
    public void arsaMetrekareAlanina_Gidilir() {
        Log.info("Arsa metrekare alanina gidiliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.arsaButon,15).click();
        Log.info("Arsa metrekare alanina gidildi");
    }

    @And("sorgula butonuna basilir")
    public void sorgulaButonunaBasilir() {
        ReasubleMethods.visibleWait(Driver.getDriver(),arsaPage.sorgulaButon,15).click();
        Log.info("Sorgula butonuna tiklandi");
    }

    @Then("gerekli hata mesajı cikiyor mu dogrulanir")
    public void gerekliHataMesajiCikiyorMuDogrulanir() {
        Log.info("Gerekli hata mesaji var mi kontrol ediliyor");
        String expectedMessage = "Bu alanın doldurulması zorunludur.";
        String actualMessage = ReasubleMethods.elementIcindekiText(arsaPage.hataMesaj);
        Assert.assertEquals(expectedMessage,actualMessage);
        Log.info("Hata mesaji kontrol edildi");
    }

    @When("eymir mahallesi secilir")
    public void eymirMahallesiSecilir() {
        Log.info("eymir mahallesi seciliyor");
        ReasubleMethods.selectByVisibleText(arsaPage.mahalleSec,"EYMİR MAHALLESİ");
        Log.info("Eymir mahallesi secildi");
    }

    @And("3803. cadde secilir")
    public void sokakSecilir() {
        Log.info("3803. cadde araniyor");
        ReasubleMethods.clickableWait(Driver.getDriver(),arsaPage.caddeSec,20);
        ReasubleMethods.waitAndSelectByText(Driver.getDriver(),arsaPage.caddeSec,"3803. CADDE",15);
        Log.info("3803. cadde secildi");
    }

    @And("2025 yili secilir ve sorgula butonuna tiklanir")
    public void yiliSecilir() {
        ReasubleMethods.clickableWait(Driver.getDriver(),arsaPage.yilSec,20);
        ReasubleMethods.selectByVisibleText(arsaPage.yilSec,"2025");
        arsaPage.sorgulaButon.click();
        Log.info("2025 yili secildi");
        Log.info("Sorgula butonuna tiklandi");
    }

    @Then("basliklar cikiyor mu dogrulanir")
    public void basliklarCikiyorMuDogrulanir() {
        Log.info("Basliklar cikiyor mu dogrulaniyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),arsaRayicPage.arsaRayicBaslik,15);
        Assert.assertEquals("Arsa Rayiç Değer Bilgisi", arsaRayicPage.arsaRayicBaslik.getText());
        Assert.assertEquals("Mahalle Adı", arsaRayicPage.mahalleAdi.getText());
        Assert.assertEquals("Cadde/Sokak Adı", arsaRayicPage.caddeSokakAdi.getText());
        Assert.assertEquals("Yıl", arsaRayicPage.yil.getText());
        Assert.assertEquals("Rayiç Değeri (TL)", arsaRayicPage.rayicDegeri.getText());
        Log.info("Basliklar dogrulandi");
    }


    @Then("beyan bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void beyanBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Beyan bilgi ekrani kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.beyanButon,15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.beyanYazi,15);
        Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.beyanYazi.getText());
        Log.info("Beyan bilgi ekrani kontrol edildi");
    }

    @Then("sicil bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void sicilBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Sicil bilgi ekrani kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.sicilButon,15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.sicilYazi,15);
        Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.sicilYazi.getText());
        Log.info("Sicil bilgi ekrani kontrol edildi");
    }

    @Then("tahakkuk bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahakkukBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahakkuk bilgi ekrani yazi kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.tahakkukButon,15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.tahakkukYazi,15);
        Assert.assertEquals("Sicil Kodu Bulunamadı.",yaziKontrolPage.tahakkukYazi.getText());
        Log.info("Tahakkuk bilgi ekrani kontrol edildi");
    }

    @Then("tahsilat bilgileri ekranına gidilir ve yazi kontrol edilir")
    public void tahsilatBilgileriEkraninaGidilirVeYaziKontrolEdilir() {
        Log.info("Tahsilat bilgi ekrani kontrol ediliyor");
        ReasubleMethods.visibleWait(Driver.getDriver(),tumHizmetlerPage.tahsilatButon,15).click();
        ReasubleMethods.visibleWait(Driver.getDriver(),yaziKontrolPage.tahsilatYazi,15);
        Assert.assertEquals("Herhangi bir kayıt bulunamadı.",yaziKontrolPage.tahsilatYazi.getText());
        Log.info("Tahsilat bilgi ekrani kontrol edildi");
    }

}


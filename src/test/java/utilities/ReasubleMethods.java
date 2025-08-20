package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class ReasubleMethods {

    //DÜZ BEKLEME
    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //GÖRÜNENE KADAR BEKLEME
    public static WebElement visibleWait(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    //TIKLANANA KADAR BEKLEME
    public static WebElement clickableWait(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //METNE GÖRE DROPDOWN'DAN SECİM YAPAR
    public static void selectByVisibleText(WebElement dropdownElement, String text) {
        Select select = new Select(dropdownElement);
        select.selectByVisibleText(text);
    }

    //DROPDOWN'DAKİ TÜM SEÇENEKLERİ LİSTE OLARAK DÖNER
    public static List<String> dropdownToList(WebElement dropdownElement) {
        Select select = new Select(dropdownElement);
        List<WebElement> options = select.getOptions();
        List<String> optionTexts = new ArrayList<>();
        for (WebElement option : options) {
            optionTexts.add(option.getText());
        }
        return optionTexts;
    }

    //SAYFA KAYDIRMA PİXELE GÖRE
    public static void scroll(WebDriver driver, int x, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(" + x + "," + y + ");");
    }

    //LİSTE ALFABETİK Mİ
    public static boolean alfabetikMi(List<String> list) {
        List<String> sortedList = new ArrayList<>(list); // Orijinali bozmamak için kopya oluştur
        sortedList.sort(String.CASE_INSENSITIVE_ORDER);  // Alfabetik sıraya göre sırala (büyük/küçük harf fark etmez)
        return list.equals(sortedList);                  // Orijinal liste ile sıralanmış liste aynı mı kontrol et
    }

    //TEKRAR EDİYOR MU
    public static boolean tekrarEdiyorMu(List<String> list) {
        Set<String> set = new HashSet<>(list);  // Tekrar edenleri otomatik atar
        return set.size() < list.size();
    }

    //LİSTEDEKİ SAYILAR BUYUKTEN KUCUGE SIRALI MI
    public static boolean buyuktenKucugeMi(List<String> list) {
        List<Integer> intList = list.stream()
                .map(Integer::parseInt)
                .toList();
        List<Integer> sortedList = new ArrayList<>(intList);
        sortedList.sort(Comparator.reverseOrder());

        return intList.equals(sortedList);
    }

    //ELEMENTİN İÇİNDEKİ YAZIYI ÇEKME
    public static String elementIcindekiText(WebElement element) {
        return element.getText().trim();
    }

    //DROPDOWNDA İSTEDİĞİMİZ ELEMENT YÜKLENENE KADAR BEKLE VE SEÇ
    public static void waitAndSelectByText(WebDriver driver,
                                           WebElement selectElement,
                                           String visibleText,
                                           int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // dropdown içindeki text gelene kadar bekle
        wait.until(d -> {
            Select select = new Select(selectElement);
            for (WebElement option : select.getOptions()) {
                if (option.getText().trim().equals(visibleText.trim())) {
                    return true;
                }
            }
            return false;
        });
        // bulunca seç
        Select select = new Select(selectElement);
        select.selectByVisibleText(visibleText.trim());
    }
}

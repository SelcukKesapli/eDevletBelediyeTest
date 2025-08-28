package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Collator;
import java.text.Normalizer;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReasubleMethods {

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
        Locale TR = new Locale("tr","TR");

        Collator coll = Collator.getInstance(TR);
        coll.setStrength(Collator.PRIMARY);
        coll.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        Function<String,String> norm = s -> {
            if (s == null) return "";
            String t = Normalizer.normalize(s, Normalizer.Form.NFKC);
            t = t.replace('\u00A0',' ').trim();   // NBSP -> space
            t = t.replaceAll("\\s+"," ");         // çoklu boşlukları sadeleştir
            return t;
        };

        // Placeholder’ları atıp normalize edilmiş orijinal sıra
        List<String> original = new ArrayList<>();
        for (String s : list) {
            String n = norm.apply(s);
            if (n.isEmpty()) continue;
            String low = n.toLowerCase(TR);
            if (low.equals("seçiniz") || low.equals("mahalle seçiniz") || low.equals("tümü")) continue;
            original.add(n);
        }

        // Natural+TR comparator: başta sayı varsa sayısal, sonra kalanını TR ile kıyasla
        Pattern LEADING_NUM = Pattern.compile("^\\s*(\\d+)");
        Comparator<String> cmp = (a, b) -> {
            String A = norm.apply(a), B = norm.apply(b);
            Matcher ma = LEADING_NUM.matcher(A);
            Matcher mb = LEADING_NUM.matcher(B);
            boolean na = ma.find(), nb = mb.find();
            if (na && nb) {
                int ia = Integer.parseInt(ma.group(1));
                int ib = Integer.parseInt(mb.group(1));
                if (ia != ib) return Integer.compare(ia, ib);
                String ra = A.substring(ma.end()).replaceFirst("^\\s*[\\.]?\\s*", "");
                String rb = B.substring(mb.end()).replaceFirst("^\\s*[\\.]?\\s*", "");
                return coll.compare(ra, rb);
            }
            return coll.compare(A, B);
        };

        List<String> sorted = new ArrayList<>(original);
        sorted.sort(cmp);

        return original.equals(sorted);
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

    public static boolean waitUrlToBe(WebDriver driver, String expectedUrl, int seconds) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.urlToBe(expectedUrl));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static boolean waitClickedOnce(WebDriver driver, WebElement el, int seconds) {
        String tmpUrl;
        try {
            tmpUrl = driver.getCurrentUrl();
        } catch (Exception ignore) {
            tmpUrl = "";
        }
        final String urlBefore = tmpUrl;

        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));

        return w.until(d -> {
            try {
                // elemana dokun: stale kontrolü tetiklensin
                el.isEnabled();

                String cls          = String.valueOf(el.getAttribute("class"));
                String disabledAttr = el.getAttribute("disabled");
                String ariaDisabled = el.getAttribute("aria-disabled");
                String ariaBusy     = el.getAttribute("aria-busy");
                String oneClick     = el.getAttribute("data-one-click");
                String dataSubmit   = el.getAttribute("data-submit");

                boolean hidden        = !el.isDisplayed();
                boolean disabledState = disabledAttr != null || "true".equalsIgnoreCase(ariaDisabled);
                boolean busyState     = "true".equalsIgnoreCase(ariaBusy)
                        || (cls != null && (cls.contains("disabled") || cls.contains("loading")
                        || cls.contains("is-loading") || cls.contains("busy") || cls.contains("processing")));
                boolean oneClicked    = "true".equalsIgnoreCase(oneClick) || "true".equalsIgnoreCase(dataSubmit);

                boolean urlChanged = false;
                try { urlChanged = !d.getCurrentUrl().equals(urlBefore); } catch (Exception ignore) {}

                return hidden || disabledState || busyState || oneClicked || urlChanged;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                // DOM değiştiyse: klik etkisini aldık say
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }



}

package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;                // ★
import org.openqa.selenium.JavascriptExecutor;     // ★
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;                          // ★

public class Driver {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser  = System.getProperty("browser", "chrome");
            boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
            String windowSizeProp = System.getProperty("windowSize", "1920,1080"); // ★
            int width = 1920, height = 1080;                                       // ★
            try {                                                                   // ★
                String[] wh = windowSizeProp.split(",");
                width  = Integer.parseInt(wh[0].trim());
                height = Integer.parseInt(wh[1].trim());
            } catch (Exception ignore) {}

            System.out.println("Browser: " + browser + ", Headless: " + headless + ", WindowSize: " + width + "x" + height); // ★

            try {
                switch (browser.toLowerCase()) {
                    case "firefox": {
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions fo = new FirefoxOptions();
                        if (headless) { fo.addArguments("--headless"); }            // ★
                        // FF için boyut argümanları
                        fo.addArguments("--width=" + width, "--height=" + height);   // ★
                        driver = new FirefoxDriver(fo);
                        break;
                    }
                    case "chrome":
                    default: {
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions co = new ChromeOptions();
                        if (headless) {
                            co.addArguments("--headless=new");                       // ★ (eskisi: --headless)
                        }
                        // CI dayanıklılık bayrakları
                        co.addArguments("--no-sandbox","--disable-dev-shm-usage","--disable-gpu"); // ★
                        // Pencere boyutu her zaman ver (responsive farklarını kapatmak için)
                        co.addArguments("--window-size=" + width + "," + height);    // ★
                        // CDP uyumsuzluğu için gerekliyse tut, değilse kaldırabilirsin
                        co.addArguments("--remote-allow-origins=*");
                        co.addArguments("--disable-blink-features=AutomationControlled");
                        driver = new ChromeDriver(co);
                        break;
                    }
                }

                if (driver != null) {
                    // Headless değilse, yine de istenen boyutu uygula (maksimize yerine)
                    if (!headless) {
                        try {
                            driver.manage().window().setSize(new Dimension(width, height)); // ★
                            // İstersen burayı maximize yapabilirsin ama responsive sapmasın diye setSize daha deterministik
                            // driver.manage().window().maximize();
                        } catch (Exception ignore) {}
                    }

                    // (Öneri) implicit wait = 0, explicit wait kullan
                    try {
                        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));    // ★
                    } catch (Exception ignore) {}

                    // Debug: gerçek viewport’u yaz
                    try {
                        Object w = ((JavascriptExecutor) driver).executeScript("return window.innerWidth");
                        Object h = ((JavascriptExecutor) driver).executeScript("return window.innerHeight");
                        System.out.println("Viewport(inner): " + w + "x" + h);              // ★
                    } catch (Exception ignore) {}

                    System.out.println("Driver başarıyla oluşturuldu: " + driver.getClass().getSimpleName());
                } else {
                    System.err.println("Driver oluşturulamadı!");
                }

            } catch (Exception e) {
                System.err.println("Driver oluşturma hatası: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Driver oluşturulamadı", e);
            }
        }
        return driver;
    }

    public static void closeDriver() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver kapatıldı");
            } catch (Exception e) {
                System.err.println("Driver kapatma hatası: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}

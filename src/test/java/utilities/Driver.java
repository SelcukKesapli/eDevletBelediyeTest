package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Driver {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = System.getProperty("browser", "chrome");
            boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

            System.out.println("Browser: " + browser + ", Headless: " + headless);

            try {
                switch (browser.toLowerCase()) {
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (headless) {
                            firefoxOptions.addArguments("--headless");
                        }
                        driver = new FirefoxDriver(firefoxOptions);
                        break;
                    case "chrome":
                    default:
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (headless) {
                            chromeOptions.addArguments("--headless");
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            chromeOptions.addArguments("--disable-gpu");
                            chromeOptions.addArguments("--window-size=1920,1080");
                        }
                        // CDP uyumsuzluğu için
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

                        driver = new ChromeDriver(chromeOptions);
                        break;
                }

                if (driver != null) {
                    driver.manage().window().maximize();
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

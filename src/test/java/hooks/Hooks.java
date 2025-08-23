package hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import pages.GirisPage;
import utilities.Driver;

public class Hooks {

    private static boolean loggedIn = false;

    private static String resolveBaseUrl() {
        String url = System.getProperty("baseUrl");   // TEK KAYNAK
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Base URL boş! Jenkins/Maven çağrısına -DbaseUrl=... ver.");
        }
        System.out.println("RESOLVED_BASE_URL=" + url);
        return url;
    }

    @BeforeAll
    public static void setUp() {
        Driver.getDriver().get(resolveBaseUrl());
        if (!loggedIn) {
            new GirisPage().login();  // kullanıcı adı/şifre ConfigReader içinden okunuyorsa aynen çalışır
            loggedIn = true;
        }
    }

    @Before
    public void anaSayfayaGit() {
        Driver.getDriver().get(resolveBaseUrl());
    }

    @AfterAll
    public static void tearDown() {
        Driver.closeDriver();
    }
}

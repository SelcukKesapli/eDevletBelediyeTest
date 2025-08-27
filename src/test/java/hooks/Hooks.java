package hooks;

import io.cucumber.java.*;
import io.qameta.allure.Allure;
import pages.GirisPage;
import utilities.Driver;
import utilities.ScenarioFail;

import static org.junit.Assert.fail;

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

    @After
    public void afterScenario(Scenario sc) {
        if (ScenarioFail.any()){
            String m = ScenarioFail.msg();
            Allure.addAttachment(sc.getName(),m);
            ScenarioFail.clear();
            fail(m);
        }
    }

    @AfterAll
    public static void tearDown() {
        Driver.closeDriver();
    }
}

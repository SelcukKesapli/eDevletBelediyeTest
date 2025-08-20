package hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import pages.GirisPage;
import utilities.ConfigReader;
import utilities.Driver;

public class Hooks {

    private static boolean loggedIn = false;

    @BeforeAll
    public static void setUp() {
        String url = ConfigReader.getProperty("base_url");
        Driver.getDriver().get(url);

        if (!loggedIn) {
            GirisPage girisPage = new GirisPage();
            girisPage.login();
            loggedIn = true;
        }
    }

    @Before
    public void anaSayfayaGit(){
        Driver.getDriver().get(ConfigReader.getProperty("base_url"));
    }

    @AfterAll
    public static void tearDown() {
        Driver.closeDriver();
    }
}

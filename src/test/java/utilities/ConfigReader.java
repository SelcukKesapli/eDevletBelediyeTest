package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        // 1) target/test-classes (classpath)
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("configuration.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) { }

        // 2) Lokal koşum: src/test/resources
        if (props.isEmpty()) {
            try (InputStream fis = new FileInputStream("src/test/resources/configuration.properties")) {
                props.load(fis);
            } catch (Exception e) {
                System.out.println("[ConfigReader] configuration.properties bulunamadı.");
            }
        }

        // Debug: anahtarları göster (değerleri göstermiyoruz)
        System.out.println("[ConfigReader] Yüklenen anahtarlar: " + props.keySet());
    }

    public static String getProperty(String key) {
        String val = props.getProperty(key);

        // Jenkins ENV fallback (özellikle bu 2 anahtar için)
        if ((val == null || val.isBlank()) && "kullanici_adi".equals(key)) {
            val = System.getenv("EDEVLET_TC");
        }
        if ((val == null || val.isBlank()) && "sifre".equals(key)) {
            val = System.getenv("EDEVLET_SIFRE");
        }
        return val;
    }

    public static String require(String key) {
        String v = getProperty(key);
        if (v == null) {
            throw new RuntimeException("[ConfigReader] Eksik anahtar: " + key);
        }
        return v;
    }

    private ConfigReader() { }
}

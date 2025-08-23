package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        // 1) classpath (target/test-classes)
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("configuration.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) { }

        // 2) lokal koşum (src/test/resources)
        if (props.isEmpty()) {
            try (InputStream fis = new FileInputStream("src/test/resources/configuration.properties")) {
                props.load(fis);
            } catch (Exception e) {
                System.out.println("[ConfigReader] configuration.properties bulunamadı.");
            }
        }

        // BOM temizliği (Windows yazımlarında görünebilir)
        stripBomFromKeysAndValues();

        // Debug: anahtar listesi (değerleri göstermiyoruz)
        System.out.println("[ConfigReader] Yüklenen anahtarlar: " + props.keySet());
    }

    private static void stripBomFromKeysAndValues() {
        List<Object> keys = new ArrayList<>(props.keySet());
        for (Object kObj : keys) {
            String k = String.valueOf(kObj);
            String v = props.getProperty(k);
            String nk = stripBom(k);
            String nv = v == null ? null : stripBom(v);
            if (!nk.equals(k)) {
                props.remove(k);
                props.setProperty(nk, nv == null ? v : nv);
            } else if (nv != null && !nv.equals(v)) {
                props.setProperty(k, nv);
            }
        }
    }

    private static String stripBom(String s) {
        if (s == null) return null;
        return s.replace("\uFEFF", "");
    }

    public static String getProperty(String key) {
        String val = props.getProperty(key);
        if (val != null) val = stripBom(val);

        // Jenkins Credentials fallback'ları
        if ((val == null || val.isBlank()) && "kullanici_adi".equals(key)) {
            val = System.getenv("EDEVLET_TC");
        }
        if ((val == null || val.isBlank()) && "sifre".equals(key)) {
            val = System.getenv("EDEVLET_SIFRE");
        }

        // base_url için: dosya yoksa System property ve default
        if ("base_url".equals(key)) {
            if (val == null || val.isBlank()) {
                val = System.getProperty("baseUrl"); // mvn -DbaseUrl=...
            }
            if (val == null || val.isBlank()) {
                val = "https://www.turkiye.gov.tr/"; // son çare
            }
        }
        return val;
    }

    public static String require(String key) {
        String v = getProperty(key);
        if (v == null || v.isBlank()) {
            throw new RuntimeException("[ConfigReader] Eksik anahtar: " + key);
        }
        return v;
    }

    private ConfigReader() { }
}

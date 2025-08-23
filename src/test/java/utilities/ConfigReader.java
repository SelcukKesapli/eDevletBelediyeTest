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
    }

    // Değer varsa döner; yoksa null
    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    // Kritik değerler için: yoksa basit hata fırlat
    public static String require(String key) {
        String v = getProperty(key);
        if (v == null) {
            throw new RuntimeException("[ConfigReader] Eksik anahtar: " + key);
        }
        return v;
    }

    private ConfigReader() { }
}

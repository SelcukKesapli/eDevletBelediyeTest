package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        // 1) target/test-classes içindeki (classpath) configuration.properties
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("configuration.properties")) {
            if (is != null) {
                props.load(is);
                return;
            }
        } catch (Exception ignored) {}

        // 2) Lokal çalışırken: src/test/resources/configuration.properties
        try (InputStream is = new FileInputStream("src/test/resources/configuration.properties")) {
            props.load(is);
        } catch (Exception e) {
            System.out.println("[ConfigReader] configuration.properties bulunamadı.");
        }
    }

    /** Varsa değeri döner; yoksa null döner. */
    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    /** Değer yoksa basit bir RuntimeException fırlatır. */
    public static String require(String key) {
        String v = getProperty(key);
        if (v == null) {
            throw new RuntimeException("[ConfigReader] Eksik anahtar: " + key);
        }
        return v;
    }

    private ConfigReader() { }
}

package ua.lviv.iot;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Клас для автоматичного зчитування версії програми з pom.xml.
 */
public final class AppVersion {

    private static final String DEFAULT_VERSION = "1.3.0";

    private AppVersion() {
    }

    /**
     * Повертає версію проєкту, зчитану з файлу ресурсів app.properties.
     *
     * @return версія проєкту або значення за замовчуванням
     */
    public static String getVersion() {
        InputStream input = AppVersion.class.getClassLoader().getResourceAsStream("app.properties");
        if (input == null) {
            return DEFAULT_VERSION;
        }

        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Properties prop = new Properties();
            prop.load(reader);
            return prop.getProperty("version", DEFAULT_VERSION);
        } catch (Exception e) {
            return DEFAULT_VERSION;
        }
    }
}
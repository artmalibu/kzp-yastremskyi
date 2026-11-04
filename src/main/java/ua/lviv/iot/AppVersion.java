package ua.lviv.iot;

import java.io.InputStream;
import java.util.Properties;

/**
 * Клас для автоматичного зчитування версії програми з pom.xml.
 */
public final class AppVersion {

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
            return "1.0-SNAPSHOT";
        }

        try (InputStream is = input) {
            Properties prop = new Properties();
            prop.load(is);
            return prop.getProperty("version", "1.0-SNAPSHOT");
        } catch (Exception e) {
            return "1.0-SNAPSHOT";
        }
    }
}
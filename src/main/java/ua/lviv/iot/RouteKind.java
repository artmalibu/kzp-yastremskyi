package ua.lviv.iot;

import java.util.Locale;
import java.util.Objects;

/**
 * Перелічуваний тип видів туристичних маршрутів (Варіант 26).
 */
public enum RouteKind {
    HIKING("піший"),
    CYCLING("велосипедний");

    private final String title;

    RouteKind(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Перетворює текстове позначення виду маршруту на значення enum.
     *
     * @param raw текстове представлення (наприклад, "HIKING", "піший", "CYCLING", "велосипедний")
     * @return відповідне значення RouteKind
     */
    public static RouteKind fromString(String raw) {
        Objects.requireNonNull(raw, "Вид маршруту не може бути null");
        String normalized = raw.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "hiking", "піший" -> HIKING;
            case "cycling", "велосипедний" -> CYCLING;
            default -> throw new IllegalArgumentException("Невідомий вид маршруту: " + raw);
        };
    }
}
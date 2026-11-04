package ua.lviv.iot;

import java.util.Objects;

/**
 * Record для збереження підсумкових показників обробки туристичних маршрутів.
 *
 * @param validCount        кількість коректних маршрутів
 * @param averageLengthKm   середня довжина маршруту у км
 * @param maxElevationM     максимальний набір висоти у м
 * @param totalDays         сумарна тривалість у днях
 */
public record RouteSummary(int validCount, double averageLengthKm, int maxElevationM, int totalDays) {

    /**
     * Канонічний конструктор з валідацією показників.
     */
    public RouteSummary {
        if (validCount < 0 || averageLengthKm < 0 || maxElevationM < 0 || totalDays < 0
                || !Double.isFinite(averageLengthKm)) {
            throw new IllegalArgumentException("Показники звіту не можуть бути від'ємними або нескінченними");
        }
    }

    /**
     * Статична фабрика для обчислення показників зі списку об'єктів Route.
     *
     * @param routes список коректних маршрутів
     * @return новий об'єкт RouteSummary
     */
    public static RouteSummary fromRoutes(java.util.List<Route> routes) {
        Objects.requireNonNull(routes, "Список маршрутів не може бути null");
        if (routes.isEmpty()) {
            return new RouteSummary(0, 0.0, 0, 0);
        }

        double sumLength = 0.0;
        int maxElevation = 0;
        int sumDays = 0;

        for (Route r : routes) {
            sumLength += r.getLengthKm();
            maxElevation = Math.max(maxElevation, r.getElevationM());
            sumDays += r.getDays();
        }

        double avgLength = sumLength / routes.size();
        return new RouteSummary(routes.size(), avgLength, maxElevation, sumDays);
    }
}
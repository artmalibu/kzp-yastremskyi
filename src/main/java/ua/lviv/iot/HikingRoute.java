package ua.lviv.iot;

import java.util.Locale;

/**
 * Підтип туристичного маршруту: піший маршрут (Варіант 26).
 */
public final class HikingRoute extends Route {

    private final boolean guideRequired;

    /**
     * Створює піший туристичний маршрут.
     *
     * @param name          назва маршруту
     * @param lengthKm      довжина в км (> 0)
     * @param elevationM    набір висоти в м (>= 0)
     * @param difficulty    складність (1..5)
     * @param days          тривалість у днях (> 0)
     * @param guideRequired чи потрібен супроводжуючий провідник
     */
    public HikingRoute(String name, double lengthKm, int elevationM, int difficulty, int days, boolean guideRequired) {
        super(name, lengthKm, elevationM, difficulty, days, RouteKind.HIKING);
        this.guideRequired = guideRequired;
    }

    /**
     * Перевантажений конструктор зі стандартним визначенням потреби у провіднику.
     */
    public HikingRoute(String name, double lengthKm, int elevationM, int difficulty, int days) {
        this(name, lengthKm, elevationM, difficulty, days, difficulty >= 4);
    }

    public boolean isGuideRequired() {
        return guideRequired;
    }

    @Override
    public double calculateComplexityScore() {
        double baseScore = getDifficulty() * 10.0 + getLengthKm() * 0.5 + (getElevationM() / 100.0);
        return guideRequired ? baseScore * 1.15 : baseScore;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s, провідник: %s",
                super.toString(), guideRequired ? "потрібен" : "не потрібен");
    }
}
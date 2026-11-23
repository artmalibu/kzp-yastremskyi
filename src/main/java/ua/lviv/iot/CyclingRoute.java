package ua.lviv.iot;

import java.util.Locale;

/**
 * Підтип туристичного маршруту: велосипедний маршрут (Варіант 26).
 */
public final class CyclingRoute extends Route {

    private final boolean pavedSurface;

    /**
     * Створює велосипедний туристичний маршрут.
     *
     * @param name         назва маршруту
     * @param lengthKm     довжина в км (> 0)
     * @param elevationM   набір висоти в м (>= 0)
     * @param difficulty   складність (1..5)
     * @param days         тривалість у днях (> 0)
     * @param pavedSurface чи має маршрут переважно асфальтоване покриття
     */
    public CyclingRoute(String name, double lengthKm, int elevationM, int difficulty, int days, boolean pavedSurface) {
        super(name, lengthKm, elevationM, difficulty, days, RouteKind.CYCLING);
        this.pavedSurface = pavedSurface;
    }

    /**
     * Перевантажений конструктор зі стандартним типом покриття.
     */
    public CyclingRoute(String name, double lengthKm, int elevationM, int difficulty, int days) {
        this(name, lengthKm, elevationM, difficulty, days, elevationM < 1000);
    }

    public boolean isPavedSurface() {
        return pavedSurface;
    }

    @Override
    public double calculateComplexityScore() {
        double baseScore = getDifficulty() * 8.0 + getLengthKm() * 0.25 + (getElevationM() / 80.0);
        return pavedSurface ? baseScore : baseScore * 1.25;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s, покриття: %s",
                super.toString(), pavedSurface ? "асфальт" : "ґрунт/бездоріжжя");
    }
}
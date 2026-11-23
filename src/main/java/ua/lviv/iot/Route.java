package ua.lviv.iot;

import java.util.Locale;
import java.util.Objects;

/**
 * Абстрактний запечатаний базовий клас туристичного маршруту (Варіант 26).
 */
public abstract sealed class Route permits HikingRoute, CyclingRoute {

    private final String name;
    private final double lengthKm;
    private final int elevationM;
    private final int difficulty;
    private final int days;
    private final RouteKind kind;

    /**
     * Конструктор базового маршруту із перевіркою інваріантів.
     *
     * @param name       назва маршруту
     * @param lengthKm   довжина в км (> 0)
     * @param elevationM набір висоти в м (>= 0)
     * @param difficulty складність (1..5)
     * @param days       тривалість у днях (> 0)
     * @param kind       вид маршруту
     */
    protected Route(String name, double lengthKm, int elevationM, int difficulty, int days, RouteKind kind) {
        this(validate(name, lengthKm, elevationM, difficulty, days, kind));
    }

    private Route(ValidatedArgs valid) {
        this.name = valid.name;
        this.lengthKm = valid.lengthKm;
        this.elevationM = valid.elevationM;
        this.difficulty = valid.difficulty;
        this.days = valid.days;
        this.kind = valid.kind;
    }

    private record ValidatedArgs(
            String name,
            double lengthKm,
            int elevationM,
            int difficulty,
            int days,
            RouteKind kind) {
    }

    private static ValidatedArgs validate(
            String name, double lengthKm, int elevationM, int difficulty, int days, RouteKind kind) {
        Objects.requireNonNull(name, "Назва не може бути null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Назва не може бути порожньою");
        }
        if (lengthKm <= 0 || !Double.isFinite(lengthKm)) {
            throw new IllegalArgumentException("Довжина має бути додатним скінченним числом");
        }
        if (elevationM < 0) {
            throw new IllegalArgumentException("Набір висоти не може бути від'ємним");
        }
        if (difficulty < 1 || difficulty > 5) {
            throw new IllegalArgumentException("Складність має бути від 1 до 5");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("Тривалість має бути додатним числом");
        }
        Objects.requireNonNull(kind, "Вид маршруту не може бути null");
        return new ValidatedArgs(name, lengthKm, elevationM, difficulty, days, kind);
    }

    /**
     * Поліморфний метод розрахунку інтегрального показника складності маршруту.
     *
     * @return числовий індекс складності маршруту
     */
    public abstract double calculateComplexityScore();

    /**
     * Статичний фабричний метод створення відповідного підтипу маршруту з CSV-рядка.
     * Підтримує базовий 5-колонковий формат та розширений 6-колонковий формат із типом маршруту.
     *
     * @param line рядок формату "назва;довжина;висота;складність;дні[;вид]"
     * @return екземпляр підкласу Route (HikingRoute або CyclingRoute)
     */
    public static Route fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);
        if (fields.length != 5 && fields.length != 6) {
            throw new IllegalArgumentException("Некоректна кількість полів у рядку");
        }

        try {
            String name = fields[0].trim();
            double lengthKm = Double.parseDouble(fields[1].trim());
            int elevationM = Integer.parseInt(fields[2].trim());
            int difficulty = Integer.parseInt(fields[3].trim());
            int days = Integer.parseInt(fields[4].trim());

            RouteKind kind = (fields.length == 6)
                    ? RouteKind.fromString(fields[5])
                    : RouteKind.HIKING;

            return switch (kind) {
                case HIKING -> new HikingRoute(name, lengthKm, elevationM, difficulty, days, difficulty >= 4);
                case CYCLING -> new CyclingRoute(name, lengthKm, elevationM, difficulty, days, elevationM < 1000);
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Помилка формату числового поля", e);
        }
    }

    public String getName() {
        return name;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public int getElevationM() {
        return elevationM;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getDays() {
        return days;
    }

    public RouteKind getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(name, route.name) && kind == route.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), name, kind);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s (%s): %.2f км, %d м висоти, складність %d/5 (індекс %.2f), %d днів",
                name, kind.getTitle(), lengthKm, elevationM, difficulty, calculateComplexityScore(), days);
    }
}
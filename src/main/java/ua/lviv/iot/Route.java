package ua.lviv.iot;

import java.util.Locale;
import java.util.Objects;

/**
 * Клас-сутність, що описує незмінний туристичний маршрут (Варіант 26).
 */
public final class Route {

    private final String name;
    private final double lengthKm;
    private final int elevationM;
    private final int difficulty;
    private final int days;

    /**
     * Конструктор із перевіркою інваріантів об'єкта.
     *
     * @param name       назва маршруту
     * @param lengthKm   довжина в км (> 0)
     * @param elevationM набір висоти в м (>= 0)
     * @param difficulty складність (1..5)
     * @param days       тривалість у днях (> 0)
     */
    public Route(String name, double lengthKm, int elevationM, int difficulty, int days) {
        this.name = Objects.requireNonNull(name, "Назва не може бути null");
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

        this.lengthKm = lengthKm;
        this.elevationM = elevationM;
        this.difficulty = difficulty;
        this.days = days;
    }

    /**
     * Статичний фабричний метод створення сутності з CSV-рядка.
     *
     * @param line рядок формату "назва;довжина;висота;складність;дні"
     * @return об'єкт Route
     */
    public static Route fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException("Некоректна кількість полів у рядку");
        }

        try {
            String name = fields[0].trim();
            double lengthKm = Double.parseDouble(fields[1].trim());
            int elevationM = Integer.parseInt(fields[2].trim());
            int difficulty = Integer.parseInt(fields[3].trim());
            int days = Integer.parseInt(fields[4].trim());

            return new Route(name, lengthKm, elevationM, difficulty, days);
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

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s: %.2f км, %d м висоти, складність %d/5, %d днів",
                name, lengthKm, elevationM, difficulty, days);
    }
}
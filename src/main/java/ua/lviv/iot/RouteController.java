package ua.lviv.iot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Контролер для керування читанням, валідацією та звітністю маршрутів.
 */
public final class RouteController {

    private final List<Route> routes = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    /**
     * Обробляє список рядків із CSV-файлу.
     *
     * @param lines список рядків
     */
    public void processLines(List<String> lines) {
        routes.clear();
        errors.clear();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                Route route = Route.fromCsv(line);
                routes.add(route);
            } catch (IllegalArgumentException e) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: %s", i + 1, e.getMessage()));
            }
        }
    }

    /**
     * Формує текст підсумкового звіту.
     *
     * @return текстовий звіт
     */
    public String buildReport() {
        RouteSummary summary = RouteSummary.fromRoutes(routes);
        StringBuilder sb = new StringBuilder();

        sb.append(String.format(Locale.ROOT, "=== ЗВІТ ОБРОБКИ ТУРИСТИЧНИХ МАРШРУТІВ ===%n"));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Коректних записів:", summary.validCount()));
        sb.append(String.format(Locale.ROOT, "%-30s %.2f км%n", "Середня довжина:", summary.averageLengthKm()));
        sb.append(String.format(Locale.ROOT, "%-30s %d м%n", "Максимальний набір висоти:", summary.maxElevationM()));
        sb.append(String.format(Locale.ROOT, "%-30s %d днів%n", "Сумарна тривалість:", summary.totalDays()));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Помилок у файлі:", errors.size()));

        if (!errors.isEmpty()) {
            sb.append(String.format(Locale.ROOT, "%nДеталі помилок:%n"));
            for (String err : errors) {
                sb.append(String.format(Locale.ROOT, "- %s%n", err));
            }
        }

        return sb.toString();
    }

    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
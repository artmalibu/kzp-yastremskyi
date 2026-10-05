package ua.lviv.iot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас програми обробки даних про туристичні маршрути (Варіант 26).
 */
public final class Main {

    private static final String VERSION = "1.0.0";

    /* Забороняє створення екземплярів службового класу. */
    private Main() {
    }

    /**
     * Точка входу в програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equals(args[i])) {
                System.out.println("Використання: java -jar lab01.jar [--help] [--version] [--input <шлях>] [--output <шлях>]");
                return;
            }
            if ("--version".equals(args[i])) {
                System.out.printf("Версія програми: %s%n", VERSION);
                return;
            }
            if ("--input".equals(args[i]) && i + 1 < args.length) {
                inputPath = Path.of(args[++i]);
            }
            if ("--output".equals(args[i]) && i + 1 < args.length) {
                outputPath = Path.of(args[++i]);
            }
        }

        try {
            List<String> lines = FileReport.readLines(inputPath);
            String reportText = processAndBuildReport(lines);

            System.out.print(reportText);
            FileReport.writeReport(outputPath, reportText);

        } catch (IOException e) {
            System.err.printf("Помилка роботи з файлом: %s%n", e.getMessage());
        }
    }

    /**
     * Парсить рядки, обчислює показники варіанта 26 та будує підсумковий звіт.
     *
     * @param lines список рядків із CSV-файла
     * @return сформований текст звіту
     */
    public static String processAndBuildReport(List<String> lines) {
        List<String> errors = new ArrayList<>();
        int validCount = 0;
        double totalLengthKm = 0.0;
        int maxElevationM = Integer.MIN_VALUE;
        int totalDays = 0;

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] fields = line.split(";", -1);
            if (fields.length != 5) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: очікується 5 полів, отримано %d", index + 1, fields.length));
                continue;
            }

            String name = fields[0].trim();
            if (name.isEmpty()) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: порожня назва маршруту", index + 1));
                continue;
            }

            try {
                double lengthKm = Double.parseDouble(fields[1].trim());
                int elevationM = Integer.parseInt(fields[2].trim());
                int difficulty = Integer.parseInt(fields[3].trim());
                int days = Integer.parseInt(fields[4].trim());

                if (lengthKm <= 0 || elevationM < 0 || difficulty <= 0 || days <= 0) {
                    errors.add(String.format(Locale.ROOT, "Рядок %d: від'ємне або нульове числове значення", index + 1));
                    continue;
                }

                validCount++;
                totalLengthKm += lengthKm;
                maxElevationM = Math.max(maxElevationM, elevationM);
                totalDays += days;

            } catch (NumberFormatException e) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: некоректне числове поле", index + 1));
            }
        }

        double averageLengthKm = validCount == 0 ? 0.0 : totalLengthKm / validCount;
        int actualMaxElevation = validCount == 0 ? 0 : maxElevationM;

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append(String.format(Locale.ROOT, "=== ЗВІТ ОБРОБКИ ТУРИСТИЧНИХ МАРШРУТІВ ===%n"));
        reportBuilder.append(String.format(Locale.ROOT, "%-30s %d%n", "Коректних записів:", validCount));
        reportBuilder.append(String.format(Locale.ROOT, "%-30s %.2f км%n", "Середня довжина:", averageLengthKm));
        reportBuilder.append(String.format(Locale.ROOT, "%-30s %d м%n", "Максимальний набір висоти:", actualMaxElevation));
        reportBuilder.append(String.format(Locale.ROOT, "%-30s %d днів%n", "Сумарна тривалість:", totalDays));
        reportBuilder.append(String.format(Locale.ROOT, "%-30s %d%n", "Помилок у файлі:", errors.size()));

        if (!errors.isEmpty()) {
            reportBuilder.append(String.format(Locale.ROOT, "%nДеталі помилок:%n"));
            for (String error : errors) {
                reportBuilder.append(String.format(Locale.ROOT, "- %s%n", error));
            }
        }

        return reportBuilder.toString();
    }
}
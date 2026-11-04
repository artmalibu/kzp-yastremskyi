package ua.lviv.iot;

import java.util.List;

/**
 * Головний клас-точки входу CLI.
 */
public final class Main {

    private Main() {
    }

    /**
     * Точка входу в програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser(args);

        if (parser.isHelpRequested()) {
            printHelp();
            return;
        }

        if (parser.isVersionRequested()) {
            System.out.println("Версія програми: " + AppVersion.getVersion());
            return;
        }

        try {
            List<String> lines = FileReport.readLines(parser.getInputPath());
            RouteController controller = new RouteController();
            controller.processLines(lines);

            String report = controller.buildReport();
            System.out.print(report);

            FileReport.writeReport(parser.getOutputPath(), report);
        } catch (Exception e) {
            System.err.println("Помилка під час виконання програми: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Консольний застосунок для обробки туристичних маршрутів (Варіант 26).");
        System.out.println("Використання:");
        System.out.println("  --help             показати цю довідку");
        System.out.println("  --version          показати версію проєкту");
        System.out.println("  --input <файл>     шлях до вхідного CSV файла (за замовчуванням data/input.csv)");
        System.out.println("  --output <файл>    шлях до згенерованого звіту (за замовчуванням out/report.txt)");
    }
}
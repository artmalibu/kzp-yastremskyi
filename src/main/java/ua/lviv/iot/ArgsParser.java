package ua.lviv.iot;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Клас для розбору аргументів командного рядка.
 */
public final class ArgsParser {

    private final Path inputPath;
    private final Path outputPath;
    private final boolean helpRequested;
    private final boolean versionRequested;

    /**
     * Розбирає масив аргументів CLI.
     *
     * @param args аргументи командного рядка
     */
    public ArgsParser(String[] args) {
        Path input = Paths.get("data", "input.csv");
        Path output = Paths.get("out", "report.txt");
        boolean help = false;
        boolean version = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help" -> help = true;
                case "--version" -> version = true;
                case "--input" -> {
                    if (i + 1 < args.length) {
                        input = Paths.get(args[++i]);
                    }
                }
                case "--output" -> {
                    if (i + 1 < args.length) {
                        output = Paths.get(args[++i]);
                    }
                }
                default -> {
                    // Невідомі аргументи ігноруються
                }
            }
        }

        this.inputPath = input;
        this.outputPath = output;
        this.helpRequested = help;
        this.versionRequested = version;
    }

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }

    public boolean isVersionRequested() {
        return versionRequested;
    }
}
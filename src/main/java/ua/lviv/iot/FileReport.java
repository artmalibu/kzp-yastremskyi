package ua.lviv.iot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Ізолює кросплатформне читання та запис текстових файлів у кодуванні UTF-8.
 */
public final class FileReport {

    /* Забороняє створення екземплярів службового класу. */
    private FileReport() {
    }

    /**
     * Читає рядки з файла у кодуванні UTF-8.
     *
     * @param input шлях до вхідного файла
     * @return список рядків файла
     * @throws IOException якщо файл неможливо прочитати
     */
    public static List<String> readLines(Path input) throws IOException {
        return Files.readAllLines(input, StandardCharsets.UTF_8);
    }

    /**
     * Створює необхідні каталоги та записує текст звіту у файл (UTF-8).
     *
     * @param output шлях до файла звіту
     * @param report готовий текст звіту
     * @throws IOException якщо виникла помилка під час створення каталогу чи запису
     */
    public static void writeReport(Path output, String report) throws IOException {
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(output, report, StandardCharsets.UTF_8);
    }
}
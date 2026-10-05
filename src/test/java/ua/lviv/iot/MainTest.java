package ua.lviv.iot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testProcessAndBuildReportWithValidData() {
        List<String> lines = List.of(
            "Чорногірський хребет;55.5;2050;4;3",
            "Мармароси;42.0;1600;3;2"
        );

        String report = Main.processAndBuildReport(lines);

        assertTrue(report.contains("Коректних записів:             2"));
        assertTrue(report.contains("Середня довжина:               48.75 км"));
        assertTrue(report.contains("Максимальний набір висоти:     2050 м"));
        assertTrue(report.contains("Сумарна тривалість:            5 днів"));
    }

    @Test
    void testProcessAndBuildReportWithInvalidData() {
        List<String> lines = List.of(
            "Боржава;помилка_числа;1000;2;2",
            ";30.0;-500;1;1"
        );

        String report = Main.processAndBuildReport(lines);

        assertTrue(report.contains("Коректних записів:             0"));
        assertTrue(report.contains("Помилок у файлі:               2"));
    }
}
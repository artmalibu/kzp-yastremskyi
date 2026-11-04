package ua.lviv.iot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testRouteControllerWithValidData() {
        List<String> lines = List.of(
            "Чорногірський хребет;55.5;2050;4;3",
            "Мармароси;42.0;1600;3;2"
        );

        RouteController controller = new RouteController();
        controller.processLines(lines);
        String report = controller.buildReport();

        assertTrue(report.contains("Коректних записів:             2"));
        assertTrue(report.contains("Середня довжина:               48.75 км"));
        assertTrue(report.contains("Максимальний набір висоти:     2050 м"));
        assertTrue(report.contains("Сумарна тривалість:            5 днів"));
        assertEquals(2, controller.getRoutes().size());
        assertTrue(controller.getErrors().isEmpty());
    }

    @Test
    void testRouteControllerWithInvalidData() {
        List<String> lines = List.of(
            "Боржава;помилка_числа;1000;2;2",
            ";30.0;-500;1;1"
        );

        RouteController controller = new RouteController();
        controller.processLines(lines);
        String report = controller.buildReport();

        assertTrue(report.contains("Коректних записів:             0"));
        assertTrue(report.contains("Помилок у файлі:               2"));
        assertEquals(0, controller.getRoutes().size());
        assertEquals(2, controller.getErrors().size());
    }

    @Test
    void testArgsParserDefaults() {
        ArgsParser parser = new ArgsParser(new String[]{});
        assertFalse(parser.isHelpRequested());
        assertFalse(parser.isVersionRequested());
        assertEquals("input.csv", parser.getInputPath().getFileName().toString());
        assertEquals("report.txt", parser.getOutputPath().getFileName().toString());
    }

    @Test
    void testArgsParserFlags() {
        ArgsParser parser = new ArgsParser(new String[]{"--help", "--version"});
        assertTrue(parser.isHelpRequested());
        assertTrue(parser.isVersionRequested());
    }
}
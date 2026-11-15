package ua.lviv.iot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RouteTest {

    @Test
    void testValidRouteCreation() {
        Route route = new Route("Чорногора", 55.5, 2050, 4, 3);
        assertEquals("Чорногора", route.getName());
        assertEquals(55.5, route.getLengthKm());
        assertEquals(2050, route.getElevationM());
        assertEquals(4, route.getDifficulty());
        assertEquals(3, route.getDays());
    }

    @Test
    void testFromCsvValid() {
        Route route = Route.fromCsv("Мармароси;42.0;1600;3;2");
        assertEquals("Мармароси", route.getName());
        assertEquals(42.0, route.getLengthKm());
    }

    @Test
    void testConstructorInvariants() {
        assertThrows(IllegalArgumentException.class, () -> new Route("", 10.0, 100, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> new Route("Тест", -5.0, 100, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> new Route("Тест", 10.0, -10, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> new Route("Тест", 10.0, 100, 6, 1));
        assertThrows(IllegalArgumentException.class, () -> new Route("Тест", 10.0, 100, 2, 0));
    }

    @Test
    void testFromCsvInvalidFieldsCount() {
        assertThrows(IllegalArgumentException.class, () -> Route.fromCsv("Мармароси;42.0;1600;3"));
    }

    @Test
    void testToStringFormatting() {
        Route route = new Route("Спиці", 18.2, 950, 2, 1);
        String text = route.toString();
        assertTrue(text.contains("18.20 км"));
        assertTrue(text.contains("950 м висоти"));
    }
}
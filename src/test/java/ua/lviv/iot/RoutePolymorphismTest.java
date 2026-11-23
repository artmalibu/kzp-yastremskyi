package ua.lviv.iot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Модульні тести поліморфізму, перелічуваного типу RouteKind та рівності у HashSet (ЛР №3).
 */
class RoutePolymorphismTest {

    @Test
    void shouldDispatchPolymorphicComplexityWithoutInstanceOf() {
        List<Route> routes = List.of(
                new HikingRoute("Говерла", 12.0, 900, 4, 1, true),
                new CyclingRoute("Сколівські Бескиди", 45.0, 600, 3, 1, true),
                new CyclingRoute("Боржава Ендуро", 38.0, 1300, 5, 2, false)
        );

        double totalComplexity = 0.0;
        for (Route route : routes) {
            totalComplexity += route.calculateComplexityScore();
        }

        // Hiking: (4*10 + 12*0.5 + 900/100) * 1.15 = 55.0 * 1.15 = 63.25
        // Cycling paved: 3*8 + 45*0.25 + 600/80 = 24 + 11.25 + 7.5 = 42.75
        // Cycling unpaved: (5*8 + 38*0.25 + 1300/80) * 1.25 = (40 + 9.5 + 16.25) * 1.25 = 82.1875
        assertEquals(63.25, routes.get(0).calculateComplexityScore(), 0.001);
        assertEquals(42.75, routes.get(1).calculateComplexityScore(), 0.001);
        assertEquals(82.1875, routes.get(2).calculateComplexityScore(), 0.001);
        assertEquals(188.1875, totalComplexity, 0.001);
    }

    @Test
    void shouldParseBothCsvFormatsAndCreateExpectedSubtypes() {
        Route defaultHiking = Route.fromCsv("Піп Іван;24.5;1100;4;2");
        Route explicitCycling = Route.fromCsv("Львів — Жовква;35.0;250;2;1;CYCLING");
        Route ukrainianCycling = Route.fromCsv("Опір;28.0;400;3;1;велосипедний");

        assertInstanceOf(HikingRoute.class, defaultHiking);
        assertEquals(RouteKind.HIKING, defaultHiking.getKind());
        assertTrue(((HikingRoute) defaultHiking).isGuideRequired());

        assertInstanceOf(CyclingRoute.class, explicitCycling);
        assertEquals(RouteKind.CYCLING, explicitCycling.getKind());
        assertTrue(((CyclingRoute) explicitCycling).isPavedSurface());

        assertInstanceOf(CyclingRoute.class, ukrainianCycling);
        assertEquals(RouteKind.CYCLING, ukrainianCycling.getKind());
    }

    @Test
    void shouldThrowExceptionForUnknownRouteKind() {
        assertThrows(IllegalArgumentException.class, () -> RouteKind.fromString("плавальний"));
        assertThrows(IllegalArgumentException.class, () -> Route.fromCsv("Тест;10.0;100;2;1;UNKNOWN"));
    }

    @Test
    void shouldMaintainEqualityAndUniquenessInHashSet() {
        Route hiking1 = new HikingRoute("Синевир", 18.0, 500, 2, 1, false);
        Route hiking2 = new HikingRoute("Синевир", 20.0, 600, 3, 2, true);
        Route cyclingSameName = new CyclingRoute("Синевир", 18.0, 500, 2, 1, true);

        assertEquals(hiking1, hiking2);
        assertEquals(hiking1.hashCode(), hiking2.hashCode());
        assertNotEquals(hiking1, cyclingSameName);
        assertFalse(hiking1.equals(null));

        Set<Route> uniqueRoutes = new HashSet<>();
        uniqueRoutes.add(hiking1);
        uniqueRoutes.add(hiking2);
        uniqueRoutes.add(cyclingSameName);

        assertEquals(2, uniqueRoutes.size());
    }

    @Test
    void shouldIncludeSubtypeDetailsInToString() {
        Route hiking = new HikingRoute("Петрос", 15.0, 1050, 4, 1, true);
        Route cycling = new CyclingRoute("Трускавець", 30.0, 320, 2, 1, true);

        assertTrue(hiking.toString().contains("піший"));
        assertTrue(hiking.toString().contains("провідник: потрібен"));
        assertTrue(cycling.toString().contains("велосипедний"));
        assertTrue(cycling.toString().contains("покриття: асфальт"));
    }
}
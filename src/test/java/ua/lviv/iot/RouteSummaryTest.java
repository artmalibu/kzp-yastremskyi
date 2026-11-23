package ua.lviv.iot;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RouteSummaryTest {

    @Test
    void shouldCreateValidRecordAndCheckEquality() {
        RouteSummary s1 = new RouteSummary(3, 38.57, 2050, 6);
        RouteSummary s2 = new RouteSummary(3, 38.57, 2050, 6);

        assertEquals(3, s1.validCount());
        assertEquals(38.57, s1.averageLengthKm(), 0.001);
        assertEquals(2050, s1.maxElevationM());
        assertEquals(6, s1.totalDays());
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void shouldThrowExceptionForInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(-1, 10.0, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(1, -5.0, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(1, Double.NaN, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(1, Double.POSITIVE_INFINITY, 100, 1));
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(1, 10.0, -50, 1));
        assertThrows(IllegalArgumentException.class, () -> new RouteSummary(1, 10.0, 100, -2));
    }

    @Test
    void shouldCalculateSummaryFromRoutesList() {
        List<Route> routes = List.of(
                new HikingRoute("Чорногірський хребет", 55.5, 2050, 4, 3),
                new HikingRoute("Мармароси", 42.0, 1600, 3, 2),
                new HikingRoute("Спиці та Ребра", 18.2, 950, 2, 1)
        );

        RouteSummary summary = RouteSummary.fromRoutes(routes);

        assertEquals(3, summary.validCount());
        assertEquals(38.566, summary.averageLengthKm(), 0.01);
        assertEquals(2050, summary.maxElevationM());
        assertEquals(6, summary.totalDays());
    }

    @Test
    void shouldHandleEmptyAndNullListInFromRoutes() {
        RouteSummary emptySummary = RouteSummary.fromRoutes(List.of());
        assertEquals(0, emptySummary.validCount());
        assertEquals(0.0, emptySummary.averageLengthKm(), 0.001);
        assertEquals(0, emptySummary.maxElevationM());
        assertEquals(0, emptySummary.totalDays());

        assertThrows(NullPointerException.class, () -> RouteSummary.fromRoutes(null));
    }
}
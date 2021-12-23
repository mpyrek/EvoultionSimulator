package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {

    @Test
    void testNextNorth() {
        assertEquals(MapDirection.WEST, MapDirection.SOUTH_WEST.next());
    }

    @Test
    void testNextWest() {
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_EAST.next());
    }

    @Test
    void testNextSouth() {
        assertEquals(MapDirection.EAST, MapDirection.NORTH_EAST.next());
    }

    @Test
    void testNextEast() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH_WEST.next());
    }

    @Test
    void testPreviousNorth() {
        assertEquals(MapDirection.WEST, MapDirection.NORTH_WEST.previous());
    }

    @Test
    void testPreviousWest() {
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_WEST.previous());
    }

    @Test
    void testPreviousSouth() {
        assertEquals(MapDirection.EAST, MapDirection.SOUTH_EAST.previous());
    }

    @Test
    void testPreviousEast() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH_EAST.previous());
    }
}
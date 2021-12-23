package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    void testPrecedes() {
        assertTrue((new Vector2d(2,3).precedes(new Vector2d(3,4))));
        assertFalse((new Vector2d(2,3).precedes(new Vector2d(1,4))));
    }

    @Test
    void testFollows() {
        assertFalse((new Vector2d(2,3).follows(new Vector2d(3,4))));
        assertTrue((new Vector2d(2,3).follows(new Vector2d(1,2))));
    }

    @Test
    void testUpperRight () {
        assertEquals((new Vector2d(2,3)).upperRight(new Vector2d(2,5)), (new Vector2d(2,5)));
        assertEquals((new Vector2d(5,3)).upperRight(new Vector2d(2,5)), (new Vector2d(5,5)));
        assertEquals((new Vector2d(2,2)).upperRight(new Vector2d(2,2)), (new Vector2d(2,2)));
        assertEquals((new Vector2d(5,5)).upperRight(new Vector2d(1,1)), (new Vector2d(5,5)));
    }

    @Test
    void testLowerLeft () {
        assertEquals((new Vector2d(2,3)).lowerLeft(new Vector2d(2,5)), (new Vector2d(2,3)));
        assertEquals((new Vector2d(5,3)).lowerLeft(new Vector2d(2,5)), (new Vector2d(2,3)));
        assertEquals((new Vector2d(2,2)).lowerLeft(new Vector2d(2,2)), (new Vector2d(2,2)));
        assertEquals((new Vector2d(5,5)).lowerLeft(new Vector2d(1,1)), (new Vector2d(1,1)));
    }

    @Test
    void testAdd() {
        assertEquals((new Vector2d(2,3)).add(new Vector2d(-2,-5)), (new Vector2d(0,-2)));
        assertEquals((new Vector2d(5,3)).add(new Vector2d(2,5)), (new Vector2d(7,8)));
        assertEquals((new Vector2d(-2,-2)).add(new Vector2d(2,2)), (new Vector2d(0,0)));
        assertEquals((new Vector2d(-5,5)).add(new Vector2d(1,-1)), (new Vector2d(-4,4)));
    }

    @Test
    void testSubtract() {
        assertEquals((new Vector2d(2,3)).subtract(new Vector2d(-2,-5)), (new Vector2d(4,8)));
        assertEquals((new Vector2d(5,3)).subtract(new Vector2d(2,5)), (new Vector2d(3,-2)));
        assertEquals((new Vector2d(-2,-2)).subtract(new Vector2d(2,2)), (new Vector2d(-4,-4)));
        assertEquals((new Vector2d(-5,5)).subtract(new Vector2d(1,-1)), (new Vector2d(-6,6)));
    }

    @Test
    void testOpposite() {
        assertEquals((new Vector2d(2,3)).opposite(), (new Vector2d(-2,-3)));
        assertEquals((new Vector2d(5,3)).opposite(), (new Vector2d(-5,-3)));
        assertEquals((new Vector2d(-2,-2)).opposite(), (new Vector2d(2,2)));
        assertEquals((new Vector2d(-5,5)).opposite(), (new Vector2d(5,-5)));
    }

    @Test
    void testToString() {
        assertEquals((new Vector2d(2,3)).toString(),"(2,3)");
        assertEquals((new Vector2d(1,1)).toString(),"(1,1)");
        assertEquals((new Vector2d(-2,-3)).toString(),"(-2,-3)");
        assertEquals((new Vector2d(2,0)).toString(),"(2,0)");
    }

    @Test
    void testEquals() {
        assertTrue((new Vector2d(2,3)).equals(new Vector2d(2,3)));
        assertFalse((new Vector2d(2,3)).equals("sth other"));
    }
}

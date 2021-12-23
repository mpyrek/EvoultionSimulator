package agh.ics.oop;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTest {
    @Test
    void canMovetoTest() {
        RectangularMap rectangularMap = new RectangularMap(12,12,2,2,2,true);
        Animal A = new Animal(rectangularMap,10,10,new Vector2d(10,10),10);
        rectangularMap.place(A);
        assertTrue(rectangularMap.canMoveTo(new Vector2d(3, 4)));
        assertFalse(rectangularMap.canMoveTo(new Vector2d(12,12)));
    }

    @Test
    void placeAndObjectAtTest() {
        RectangularMap RM = new RectangularMap(5,5,2,2,2,true);
        Animal A = new Animal(RM,2,2,new Vector2d(1,1),0);
        Animal B = new Animal(RM,1,2,new Vector2d(1,1),0);
        RM.place(A);
        RM.place(B);
        assertEquals(A, RM.objectAt(A.getPosition()));
    }

    @Test
    void isOccupiedTest() {
        RectangularMap RM = new RectangularMap(12,12,2,2,2,true);
        Animal A = new Animal(RM,3,3,new Vector2d(2,2),1);
        RM.place(A);
        assertTrue(RM.isOccupied(new Vector2d(2,2)));
        assertFalse(RM.isOccupied(new Vector2d(1,1)));
    }
}
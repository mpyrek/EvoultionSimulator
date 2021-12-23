package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    @Test
    void reduceEnergy(){
        RectangularMap RM = new RectangularMap(6,6,0,0,0,true);
        Animal animal = new Animal(RM,10,2,new Vector2d(2,2),0);
        animal.genes.createGenesForAdamAndEwa();
        RM.place(animal);
        assertEquals(RM.objectAt(new Vector2d(2,2)),animal);
        animal.move();
        assertEquals(animal.getEnergyLevel(),8);
    }

    @Test
    void reproduceTest(){
        AbstractWorldMap RM = new RectangularMap(6,6,0,0,0,true);
        Animal animal = new Animal(RM,10,2,new Vector2d(2,2),0);
        Animal animal1 =  new Animal(RM,10,2,new Vector2d(2,2),0);
        animal.genes.createGenesForAdamAndEwa();
        animal1.genes.createGenesForAdamAndEwa();
        RM.place(animal);
        RM.place(animal1);
        RM.reproduce(0);
        assertEquals(RM.listOfAnimal.size(),3);
        int[] genesChild = RM.animals.get(new Vector2d(2,2)).get(2).genes.genes;
        for(int i = 0 ;i<16;i++){
            assertEquals(genesChild[i],animal.genes.genes[i]);
        }
        for(int i = 16 ;i<32;i++){
            assertEquals(genesChild[i],animal1.genes.genes[i]);
        }

    }

}

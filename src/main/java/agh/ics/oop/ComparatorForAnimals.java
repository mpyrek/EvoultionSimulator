package agh.ics.oop;

import java.util.Comparator;

public class ComparatorForAnimals implements Comparator<Animal> {

    @Override
    public int compare(Animal animal, Animal t1) {
        if (animal.energyLevel > t1.energyLevel) return -1;
        else if (animal.energyLevel < t1.energyLevel) return 1;
        return animal.hashCode() - t1.hashCode();
    }
}

package agh.ics.oop;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    public Map<Vector2d, ArrayList<Animal>> animals = new ConcurrentHashMap<>();
    public CopyOnWriteArrayList<Animal> listOfAnimal = new CopyOnWriteArrayList<>();
    public Comparator<Animal> compForAnimal = new ComparatorForAnimals();

    public abstract boolean canMoveTo(Vector2d position);

    @Override
    public boolean place(Animal animal) {
        if (animal.getPosition() != null) {
            animal.addObserver(this);
            addAnimalToMap(animal);
            listOfAnimal.add(animal);
            return true;
        } else throw new IllegalArgumentException(animal.getPosition() + " pozycja jest zabroniona");
    }

    private void addAnimalToMap(Animal animal) {
        if (animals.containsKey(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
            Collections.sort(animals.get(animal.getPosition()), compForAnimal);
        } else {
            animals.put(animal.getPosition(), new ArrayList<>());
            animals.get(animal.getPosition()).add(animal);
        }
        Collections.sort(animals.get(animal.getPosition()), compForAnimal);
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        animals.get(oldPosition).remove(element);
        addAnimalToMap((Animal) element);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public IMapElement objectAt(Vector2d position) {
        if (animals.containsKey(position)) {
            if (animals.get(position).size() > 0) return animals.get(position).get(0);
        }
        return null;
    }

    public void removeDeadAnimal(Animal animal) {
        animals.get(animal.getPosition()).remove(animal);
    }

    public void removeDeadFromList(CopyOnWriteArrayList<Animal> animalsDead) {
        for (Animal animal : animalsDead) {
            this.listOfAnimal.remove(animal);
        }
    }

    public void reproduce(int day) {
        for (Vector2d vector2d : animals.keySet()) {
            if (animals.get(vector2d).size() > 1) {
                Animal dady = animals.get(vector2d).get(0);
                Animal mam = animals.get(vector2d).get(1);
                int dadyEnergyLevel = dady.getEnergyLevel();
                int mamEnergyLevel = mam.getEnergyLevel();

                if (dadyEnergyLevel >= 0.5 * dady.startEnergy && mamEnergyLevel > mam.startEnergy * 0.5) {
                    Animal child = new Animal(this, mam.startEnergy, mam.moveEnergy, vector2d, day);
                    child.energyLevel = (int)(0.25 * dady.getEnergyLevel() + 0.25 * mam.getEnergyLevel());
                    int placeOfDivisionGenes =(int) (32*dadyEnergyLevel) / (dadyEnergyLevel + mamEnergyLevel);
                    child.genes = new Genes();
                    child.genes.createGenesForChild(dady.genes.genes, mam.genes.genes, placeOfDivisionGenes);
                    addAnimalToMap(child);
                    mam.energyLevel = (int) (mam.energyLevel * 0.75);
                    dady.energyLevel = (int) (dady.energyLevel * 0.75);
                    this.place(child);
                    dady.children.add(child);
                    mam.children.add(child);
                    if (dady.isTricking()) dady.childrenFromTheDayOfTricking.add(child);
                    else if (mam.isTricking()) mam.childrenFromTheDayOfTricking.add(child);
                }
            }
        }
    }

}

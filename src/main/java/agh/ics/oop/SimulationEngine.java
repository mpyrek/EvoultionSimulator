package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class SimulationEngine implements IEngine, IPositionChangeObserver, Runnable {
    private static int INITIAL_COUNT_OF_ANIMALS = 30;
    public final RectangularMap map;
    public final int startEnergy;
    public final int moveEnergy;
    ArrayList<App> observers;
    private boolean stopSimulation = true;
    private boolean finishSimulation = false;
    private boolean magicIsDone;
    private ArrayList<Integer> lifeExpectancy;
    private int era;
    private int counterAnimal;
    private int counterChildren;

    public SimulationEngine(RectangularMap map, int startEnergy, int moveEnergy) {
        this.map = map;
        this.lifeExpectancy = new ArrayList<>();
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;

        this.observers = new ArrayList<>();
        this.magicIsDone = false;
        addAnimals();
        this.era = 0;
        this.counterAnimal = 0;
        this.counterChildren = 0;
    }


    public void addObserver(App application) {
        this.observers.add(application);
    }

    public void positionChanged(IMapElement animal, Vector2d oldVector, Vector2d newVector) {
        for (App observer : this.observers) observer.positionChanged(animal, oldVector, newVector);
    }

    private void addAnimals() {
        for (int i = 0; i < INITIAL_COUNT_OF_ANIMALS; i++) {
            Animal animal = new Animal(this.map, this.startEnergy, this.moveEnergy, new Vector2d(
                    (int) (Math.random() * this.map.getWidth()), (int) (Math.random() * this.map.getHeight())), 0);
            animal.genes.createGenesForAdamAndEwa();
            this.map.place(animal);
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            while (!finishSimulation) {
                if (this.map.listOfAnimal.size() == 0) {
                    finishSimulation = true;
                    stopSimulation = true;
                    this.positionChanged(null, null, null);
                }

                if (!stopSimulation) {
                    era = era + 1;
                    this.magicIsDone = false;
                    move();
                    eat();
                    removeDead();
                    reproduce();

                    if (this.map.freePlaceForGrass) {
                        this.map.addGrass();
                        this.map.addGrass();
                    }
                    if (this.map.listOfAnimal.size() <= 5 && !this.map.isNormalVersion && this.map.countWhichSpell < 3) {
                        this.addMagicCreation();
                        this.map.countWhichSpell += 1;
                        this.magicIsDone = true;
                    }

                    this.positionChanged(null, null, null);
                }
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            finishSimulation = true;
        }
    }

    @Override
    public boolean isMagicDone() {
        return magicIsDone;
    }

    @Override
    public void stopSimulation() {
        stopSimulation = true;
    }

    public boolean isStopSimulation(){ return stopSimulation; }

    @Override
    public void resumeSimulation() {
        stopSimulation = false;
    }

    @Override
    public void finishSimulation() {
        finishSimulation = true;
    }

    @Override
    public boolean isFinishSimulation() { return finishSimulation; }

    @Override
    public int getCounterAnimal() { return counterAnimal; }

    @Override
    public int getCounterChildren() { return counterChildren; }

    @Override
    public int getDayOfSimulation() { return era; }


    public void move() {
        for (Animal animal : this.map.listOfAnimal) {
            animal.move();
        }
    }

    private void addMagicCreation() {
        for (Animal animal : this.map.listOfAnimal) {
            Vector2d position = new Vector2d((int) (Math.random() * this.map.getWidth()), (int) (Math.random() * this.map.getHeight()));

            while (this.map.objectAt(position) instanceof Animal) {
                position = new Vector2d((int) (Math.random() * this.map.getWidth()), (int) (Math.random() * this.map.getHeight()));
            }
            Animal magic = new Animal(this.map, this.startEnergy, this.moveEnergy, position, this.era);
            magic.genes.genes = animal.genes.genes;
            this.map.place(magic);
        }
    }

    public void eat() {
        Set<Vector2d> grassTuft = this.map.tuftGrass.keySet();
        for (Vector2d vector2d : grassTuft) {
            if (this.map.objectAt(vector2d) instanceof Animal) {
                this.map.eat(vector2d);
            }
        }
    }

    public void removeDead() {
        CopyOnWriteArrayList<Animal> listToRemove = new CopyOnWriteArrayList<>();

        for (Animal animal : this.map.listOfAnimal) {
            if (animal.getEnergyLevel() < moveEnergy) {
                animal.dayOfDeath = this.era;
                this.map.removeDeadAnimal(animal);
                this.lifeExpectancy.add(animal.dayOfDeath - animal.dayOfBorn);
                listToRemove.add(animal);
            }
        }

        if (listToRemove.size() > 0) {
            for (Animal animal : listToRemove) {
                counterChildren += animal.children.size();
            }
            counterAnimal += listToRemove.size();
            this.map.removeDeadFromList(listToRemove);
        }
    }

    public void reproduce() {
        this.map.reproduce(this.era);
    }


    @Override
    public int[] getDominateGenes() {
        if (this.map.listOfAnimal.size() > 0) {
            int[] countingSort = new int[this.map.listOfAnimal.size()];

            for (int i = 0; i < this.map.listOfAnimal.size() - 1; i++) {
                for (int j = i + 1; j < this.map.listOfAnimal.size(); j++) {
                    if (this.map.listOfAnimal.get(i).genes.equalGenes(this.map.listOfAnimal.get(j).genes))
                        countingSort[i] += 1;
                }
            }

            int maxIndex = 0;
            for (int i = 1; i < this.map.listOfAnimal.size(); i++) {
                if (countingSort[maxIndex] < countingSort[i]) maxIndex = i;
            }

            return this.map.listOfAnimal.get(maxIndex).genes.genes;
        }
        return new int[32];
    }

    @Override
    public int getAverageEnergyForAliveAnimals() {
        if (this.map.listOfAnimal.size() > 0) {
            int average = 0;
            for (Animal animal : this.map.listOfAnimal) {
                average = average + animal.getEnergyLevel();
            }
            return (average / this.map.listOfAnimal.size());
        }
        return 0;
    }

    @Override
    public int getLifeExpectancy() {
        if (this.lifeExpectancy.size() > 0) {
            int sum = 0;
            for (Integer days : this.lifeExpectancy) sum += days;
            return (int) (sum / this.lifeExpectancy.size());
        }
        return 0;
    }

    @Override
    public int getAverageCountOfChildren() {
        int sum = 0;
        if (this.map.listOfAnimal.size() > 0) {
            for (Animal animal : this.map.listOfAnimal) sum += animal.children.size();
            return (int) (sum / this.map.listOfAnimal.size());
        }
        return 0;
    }

}

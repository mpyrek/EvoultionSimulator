package agh.ics.oop;

import java.util.ArrayList;

public class Animal implements IMapElement {
    private MapDirection direct;
    protected Vector2d position;
    private IWorldMap map;
    private ArrayList<IPositionChangeObserver> observer = new ArrayList<>();
    protected Genes genes;
    protected int dayOfBorn;
    protected int dayOfDeath = -1;
    protected int energyLevel;
    protected boolean tricking = false;
    protected ArrayList<Animal> children;
    protected ArrayList<Animal> childrenFromTheDayOfTricking;
    final int startEnergy;
    final int moveEnergy;

    public Animal(IWorldMap map, int startEnergy, int moveEnergy, Vector2d position, int dayOfBorn) {
        this.map = map;
        this.position = position;
        this.genes = new Genes();
        this.direct = this.direct.values()[this.genes.drawGene()];
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.energyLevel = startEnergy;
        this.dayOfBorn = dayOfBorn;
        this.children = new ArrayList<>();
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void lowEnergy() {
        this.energyLevel = this.energyLevel - this.moveEnergy;
    }

    public void addEnergy(int platyEnergy) {
        this.energyLevel = this.energyLevel + platyEnergy;
    }

    @Override
    public boolean isAnimals() {
        return true;
    }


    public String getColor() {
        if (this.energyLevel == this.startEnergy) return "#DC143C";
        else if (this.energyLevel > 0.75 * this.startEnergy) return "#ff0000";
        else if (this.energyLevel > 0.5 * this.startEnergy) return "#db7093";
        else if (this.energyLevel > 0.25 * this.startEnergy) return "#fa8072";
        else return "#ffb6c1";
    }


    void addObserver(IPositionChangeObserver observer) {
        this.observer.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        this.observer.remove(observer);
    }

    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observ : this.observer) observ.positionChanged(this, oldPosition, newPosition);
    }

    public int[] getGenes() {
        return this.genes.genes;
    }

    public MapDirection getDirect() {
        return direct;
    }

    public Vector2d getPosition() {
        return position;
    }


    public boolean isAt(Vector2d position) {
        if (this.position.equals(position)) return true;
        else return false;
    }

    public void move() {
        if (this.energyLevel < this.moveEnergy) this.energyLevel = 0;
        else {
            this.lowEnergy();

            int whatMove = this.genes.drawGene();
            switch (whatMove) {
                case 1:
                    this.direct = this.direct.next();
                    break;
                case 2:
                    this.direct = this.direct.next().next();
                    break;
                case 3:
                    this.direct = this.direct.next().next().next();
                    break;
                case 5:
                    this.direct.previous().previous().previous();
                    break;
                case 6:
                    this.direct.previous().previous();
                    break;
                case 7:
                    this.direct.previous();
                    break;
                case 0:
                    if (this.map.canMoveTo(this.position.add(this.direct.toUnitVector()))) {
                        Vector2d vec = new Vector2d(this.position.x, this.position.y);
                        this.position = this.position.add(this.direct.toUnitVector());
                        this.positionChanged(this, vec, this.position);
                    } else {
                        this.direct.next().next().next().next();
                    }
                    break;
                case 4:
                    if (this.map.canMoveTo(this.position.subtract(this.direct.toUnitVector()))) {
                        Vector2d vec = new Vector2d(this.position.x, this.position.y);
                        this.position = this.position.subtract(this.direct.toUnitVector());
                        this.positionChanged(this, vec, this.position);
                    } else {
                        this.direct.next().next().next().next();
                    }
                    break;
            }
        }
    }

    private int countOfDescendants(Animal animal, int sum) {
        if (animal.children.size() > 0) {
            sum += animal.children.size();
            for (int i = 0; i < animal.children.size(); i++) {
                sum += countOfDescendants(animal.children.get(i), 0);
            }
        }
        return sum;
    }

    public String toStringEt() {
        int sum = this.childrenFromTheDayOfTricking.size();
        for (int i = 0; i < this.childrenFromTheDayOfTricking.size(); i++) {
            sum += this.countOfDescendants(this.childrenFromTheDayOfTricking.get(i), 0);
        }
        if (dayOfDeath != -1) {
            return "Hello I am beauty creation :) \r" +
                    "The number of my children = " + this.childrenFromTheDayOfTricking.size() + "\r" +
                    "Number of descendants = " + sum + "\r" +
                    "Sad day of my death is " + this.dayOfDeath;
        } else {
            return "Hello I am beauty creation :) \r" +
                    "The number of my children = " + this.childrenFromTheDayOfTricking.size() + "\r" +
                    "Number of descendants = " + sum + "\r" +
                    "Happily I am still alive";
        }
    }

    public boolean isTricking() {
        return tricking;
    }

    public void setTricking(boolean tricking) {
        this.tricking = tricking;
        childrenFromTheDayOfTricking = new ArrayList<>();
    }
}

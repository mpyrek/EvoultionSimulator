package agh.ics.oop;


public class Grass implements IMapElement {
    final Vector2d tuftGrassPosition;

    public Grass(Vector2d vector) { this.tuftGrassPosition = vector; }

    public Vector2d getPosition() { return this.tuftGrassPosition; }

    public String toString() {
        return "*";
    }

    @Override
    public boolean isAnimals() {
        return false;
    }

    public String getColor() {
        return "#7FFF00";
    }

}

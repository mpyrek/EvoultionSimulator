package agh.ics.oop;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RectangularMap extends AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    public Map<Vector2d, Grass> tuftGrass = new ConcurrentHashMap<>();

    public final int width;
    public final int height;
    public final int plantEnergy;
    public final int jungleWidth;
    public final int jungleHeight;
    protected boolean freePlaceForGrass = true;
    public final Vector2d lowerLeftForJungle;
    public final Vector2d upperRightForJungle;
    private final int countOfPossibleGrass;
    private int countOfGrassInJungle;
    protected boolean isNormalVersion;
    public int countWhichSpell;

    public RectangularMap(int width, int height, int jungleWidth, int jungleHeight, int plantEnergy, boolean isNormalVersion) {
        this.width = width;
        this.height = height;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.plantEnergy = plantEnergy;
        this.lowerLeftForJungle = new Vector2d(width / 2 - jungleWidth / 2, height / 2 - jungleHeight / 2);
        this.upperRightForJungle = new Vector2d(width / 2 + jungleWidth / 2, height / 2 + jungleHeight / 2);
        this.countOfPossibleGrass =
                (this.upperRightForJungle.x - this.lowerLeftForJungle.x) * (this.upperRightForJungle.y - this.lowerLeftForJungle.y);
        this.countOfGrassInJungle = 0;
        this.isNormalVersion = isNormalVersion;
        if (!this.isNormalVersion) this.countWhichSpell = 0;
        createJungle();
    }

    private void createJungle() {
        int countOfInitGrass = (this.jungleHeight * this.jungleWidth);
        for (int i = 0; i < countOfInitGrass; i++) {
            if (this.onJungle()) {
                Vector2d vectorForNewGrass = createVectorForGrass(this.lowerLeftForJungle, this.upperRightForJungle);
                if (objectAt(vectorForNewGrass) == null) {
                    Grass tuft = new Grass(vectorForNewGrass);
                    this.tuftGrass.put(vectorForNewGrass, tuft);
                    this.countOfGrassInJungle = this.countOfGrassInJungle + 1;
                }
            } else {
                Vector2d vectorForNewGrass = createVectorForGrass(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
                if (objectAt(vectorForNewGrass) == null) {
                    Grass tuft = new Grass(vectorForNewGrass);
                    this.tuftGrass.put(vectorForNewGrass, tuft);
                }
            }
        }

    }

    private boolean onJungle() {
        //probability that Grass is on jungle is 1 : 8
        int x = (int) (Math.random() * 8);
        if (x == 0) return false;
        else return true;
    }

    private Vector2d createVectorForGrass(Vector2d lowerPoint, Vector2d upperPoint) {
        return new Vector2d((int) (Math.random() * (upperPoint.x - lowerPoint.x + 1) + lowerPoint.x),
                (int) (Math.random() * (upperPoint.y - lowerPoint.y + 1) + lowerPoint.y));

    }

    public void addGrass() {
        if (this.freePlaceForGrass) {
            if (this.onJungle()) {
                Vector2d vectorForNewGrass = createVectorForGrass(this.lowerLeftForJungle, this.upperRightForJungle);
                if (this.objectAt(vectorForNewGrass) == null) {
                    Grass tuft = new Grass(vectorForNewGrass);
                    this.tuftGrass.put(vectorForNewGrass, tuft);
                    this.countOfGrassInJungle = this.countOfGrassInJungle + 1;
                }
                if (this.countOfGrassInJungle == this.countOfPossibleGrass) this.freePlaceForGrass = false;
            } else {
                Vector2d vectorForNewGrass = createVectorForGrass(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
                if (objectAt(vectorForNewGrass) == null) {
                    Grass tuft = new Grass(vectorForNewGrass);
                    this.tuftGrass.put(vectorForNewGrass, tuft);
                }
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.precedes(new Vector2d(width - 1, height - 1)) && position.follows(new Vector2d(0, 0)));
    }


    @Override
    public boolean isOccupied(Vector2d position) {
        Boolean isAnimalOccupied = super.isOccupied(position);
        if (!isAnimalOccupied) {
            if (tuftGrass.containsKey(position)) return true;
            return false;
        } else return true;
    }

    @Override
    public IMapElement objectAt(Vector2d position) {
        Animal object = (Animal) super.objectAt(position);
        if (object != null) return object;
        else {
            if (tuftGrass.containsKey(position)) return tuftGrass.get(position);
            return null;
        }
    }

    public void eat(Vector2d vector2d) {
        int size = animals.get(vector2d).size();
        for (Animal animal1 : animals.get(vector2d)) {
            animal1.addEnergy((int) (this.plantEnergy / size));
        }

        this.tuftGrass.remove(vector2d);
        if (vector2d.x >= this.lowerLeftForJungle.x && vector2d.x <= this.upperRightForJungle.x
                && vector2d.y >= this.lowerLeftForJungle.y && vector2d.y <= this.upperRightForJungle.y) {
            if (this.freePlaceForGrass == false) this.freePlaceForGrass = true;
            this.countOfGrassInJungle = this.countOfGrassInJungle - 1;
        }
    }


}

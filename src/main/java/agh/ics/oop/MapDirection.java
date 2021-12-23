package agh.ics.oop;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public String toString() {
        switch (this) {
            case EAST:
                return "Wschod";
            case SOUTH:
                return "Poludnie";
            case WEST:
                return "Zachod";
            case NORTH:
                return "Polnoc";
            default:
                return "";
        }
    }

    public MapDirection next() {
        switch (this) {
            case NORTH:
                return NORTH_EAST;
            case NORTH_EAST:
                return EAST;
            case EAST:
                return SOUTH_EAST;
            case SOUTH_EAST:
                return SOUTH;
            case SOUTH_WEST:
                return WEST;
            case WEST:
                return NORTH_WEST;
            case NORTH_WEST:
                return NORTH;
            case SOUTH:
                return SOUTH_WEST;
            default:
                throw new IllegalArgumentException(this + " is not legal move specification");
        }
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH:
                return NORTH_WEST;
            case NORTH_EAST:
                return NORTH;
            case EAST:
                return NORTH_EAST;
            case SOUTH_EAST:
                return EAST;
            case SOUTH_WEST:
                return SOUTH;
            case WEST:
                return SOUTH_WEST;
            case NORTH_WEST:
                return WEST;
            case SOUTH:
                return SOUTH_EAST;
            default:
                throw new IllegalArgumentException(this + " is not legal move specification");
        }
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case NORTH_EAST:
                return new Vector2d(1, 1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTH_EAST:
                return new Vector2d(1, -1);
            case SOUTH_WEST:
                return new Vector2d(-1, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTH_WEST:
                return new Vector2d(-1, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            default:
                throw new IllegalArgumentException(this + " is not legal move specification");
        }
    }
}

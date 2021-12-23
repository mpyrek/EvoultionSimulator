package agh.ics.oop;

import java.util.LinkedList;
import java.util.List;

public class OptionsParser {

    public MoveDirection[] parse(String[] tab) {
        List<MoveDirection> direct = new LinkedList<MoveDirection>();
        for (String t : tab) {
            if (t.equals("f") || t.equals("forward")) {
                direct.add(MoveDirection.FORWARD);
            } else if (t.equals("b") || t.equals("backward")) {
                direct.add(MoveDirection.BACKWARD);
            } else if (t.equals("r") || t.equals("right")) {
                direct.add(MoveDirection.RIGHT);
            } else if (t.equals("l") || t.equals("left")) {
                direct.add(MoveDirection.LEFT);
            } else {
                throw new IllegalArgumentException(t + " is not legal move specification");
            }
        }
        return direct.toArray(new MoveDirection[direct.size()]);
    }
}

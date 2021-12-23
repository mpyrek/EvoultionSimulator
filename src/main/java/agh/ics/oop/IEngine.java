
package agh.ics.oop;

import agh.ics.oop.gui.App;

public interface IEngine {

    void run();

    void addObserver(App a);

    void stopSimulation();

    void resumeSimulation();

    void finishSimulation();

    boolean isStopSimulation();

    boolean isFinishSimulation();

    int getDayOfSimulation();

    int[] getDominateGenes();

    int getAverageEnergyForAliveAnimals();

    int getLifeExpectancy();

    int getAverageCountOfChildren();

    boolean isMagicDone();

    int getCounterAnimal();

    int getCounterChildren();
}
package agh.ics.oop;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticWriter {
    IEngine engine;
    RectangularMap map;
    List<String[]> dataLines;
    public File csvOutputFile;
    int numberOfSave;
    int[] summaryStatistics;

    public StatisticWriter(IEngine engine, RectangularMap map) {
        this.engine = engine;
        this.map = map;
        this.numberOfSave = 0;
        this.dataLines = new ArrayList<>();
        dataLines.add(new String[]{"Day of simulation: ","All animals: ", "Dominant genotypes: ","Average energy level for alive animals: ",
                "Life expectancy of animals for dead animals: ","The average number of children for alive animals: "});

        this.summaryStatistics = new int[6];
    }

    public void translateStringToList() {
        this.numberOfSave += 1;
        dataLines.add(new String[]{
                ""+this.engine.getDayOfSimulation(),
                ""+this.map.listOfAnimal.size(),
                ""+ Arrays.toString(this.engine.getDominateGenes()),
                ""+this.engine.getAverageEnergyForAliveAnimals(),
                ""+this.engine.getLifeExpectancy(),
                ""+this.engine.getAverageCountOfChildren()
        });
        summaryStatistics[1]+=this.map.listOfAnimal.size();
        summaryStatistics[3]+=this.engine.getAverageEnergyForAliveAnimals();
        summaryStatistics[4]+=this.engine.getLifeExpectancy();
        summaryStatistics[5]+=this.engine.getAverageCountOfChildren();
    }

    public void addEndStatistic(String path){
        this.csvOutputFile = new File(path);
        dataLines.add(new String[]{
                "The simulation lasted "+this.engine.getDayOfSimulation(),
                "All animals which existed during simulation: "+this.engine.getCounterAnimal(),
                "All animals which have got parents: "+this.engine.getCounterChildren(),
                "Average count of animals on map during simulation "+summaryStatistics[1]/this.engine.getDayOfSimulation(),
                "Average energy for alive animals during simulation:  "+summaryStatistics[3]/this.engine.getDayOfSimulation(),
                "Average life expectancy: "+summaryStatistics[4]/this.engine.getDayOfSimulation(),
                "Average count of children "+summaryStatistics[5]/this.engine.getDayOfSimulation()
        });

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream().map(this::convertToCSV).forEach(pw::println);
        } catch (IOException ex) {
            System.out.println(csvOutputFile + ex.getMessage());
        }
    }

    public void saveStatisticToFile(String path) {
        this.csvOutputFile = new File(path);
        translateStringToList();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream().map(this::convertToCSV).forEach(pw::println);
        } catch (IOException ex) {
            System.out.println(csvOutputFile + ex.getMessage());
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}

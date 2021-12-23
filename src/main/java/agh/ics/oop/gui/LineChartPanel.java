package agh.ics.oop.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartPanel {

    public LineChart<Number, Number> lineChart;
    private XYChart.Series allAnimalSeries;
    private XYChart.Series allGrassSeries;
    private XYChart.Series averageEnergySeries;
    private XYChart.Series averageExpectancySeries;
    private XYChart.Series averageCountOfChildrenSeries;
    private final NumberAxis Era;
    private final NumberAxis yAxis;

    public LineChartPanel(){
        this.Era = new NumberAxis();
        yAxis = new NumberAxis();
        lineChart = new LineChart<Number, Number>(Era, yAxis);
        createLineChart();
    }

    private void createLineChart() {
        Era.setLabel("Era");

        lineChart.setTitle("See in chart");

        allAnimalSeries = new XYChart.Series();
        allAnimalSeries.setName("All animals");


        allGrassSeries = new XYChart.Series();
        allGrassSeries.setName("All Grass");

        averageEnergySeries = new XYChart.Series();
        averageEnergySeries.setName("Average Energy");

        averageExpectancySeries = new XYChart.Series();
        averageExpectancySeries.setName("Average Expectancy");

        averageCountOfChildrenSeries = new XYChart.Series();
        averageCountOfChildrenSeries.setName("Average count of children");


        lineChart.getData().addAll(allAnimalSeries, allGrassSeries, averageEnergySeries, averageExpectancySeries, averageCountOfChildrenSeries);
    }

    public void addToLineChart(int era, int allAnimals, int allGrass, int averageEnergy, int averageExpectancy, int averageCountOfChildren) {
        allAnimalSeries.getData().add(new XYChart.Data(era, allAnimals));
        allGrassSeries.getData().add(new XYChart.Data(era, allGrass));
        averageEnergySeries.getData().add(new XYChart.Data(era, averageEnergy));
        averageExpectancySeries.getData().add(new XYChart.Data(era, averageExpectancy));
        averageCountOfChildrenSeries.getData().add(new XYChart.Data(era, averageCountOfChildren));
    }
}

package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.stage.WindowEvent;
import java.util.Arrays;


public class App extends Application {
    private Button start;
    private Button stop;
    private Button saveStatistics;
    private Button showDominateGenom;
    private VBox buttons;
    private HBox app;
    private VBox animalStat = new VBox();
    private VBox allStatistic = new VBox();
    private Label statistic;
    private Animal animalToTrick = null;
    protected GridPane gridPane = new GridPane();
    private String magicAnnouncement = "A magical work has happened! \r Number of spell: ";
    private StatisticWriter statisticWriter;

    public RectangularMap map;
    public IEngine engine;
    public Thread threadEngine;
    int startRowIndex;
    int startColumnIndex;
    int endRowIndex;
    int endColumnIndex;
    boolean isNormalVersion;

    private LineChartPanel lineChartPanel;


    Stage window;
    Scene scene;
    Stage thirdStage;

    int width;
    int height;
    int jungleInPercent;
    int startEnergy;
    int moveEnergy;
    int plantEnergy;
    int jungleWidth;
    int jungleHeight;

    static int FREQUENCY_OF_SAVING_STAT = 1;


    public void make() throws Exception {
        try {
            this.map = new RectangularMap(width, height, this.jungleWidth, this.jungleHeight, this.plantEnergy, this.isNormalVersion);
            this.engine = new SimulationEngine(this.map, this.startEnergy, this.moveEnergy);
            this.threadEngine = new Thread((Runnable) this.engine);
            this.engine.addObserver(this);
            this.statisticWriter = new StatisticWriter(this.engine,this.map);

        } catch (IllegalArgumentException ex) {
            System.out.println("Problem z ustawieniem na mapie " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition){
        Platform.runLater(() -> {
            gridPane.setGridLinesVisible(false);
            gridPane.getChildren().clear();
            fillGap();
            gridPane.setGridLinesVisible(true);

            app.getChildren().remove(2);
            allStatistic.getChildren().remove(0);
            makeStatistics();
            allStatistic.getChildren().remove(1);
            lineChartPanel.addToLineChart(this.engine.getDayOfSimulation(), this.map.listOfAnimal.size(), this.map.tuftGrass.size(),
                    this.engine.getAverageEnergyForAliveAnimals(), this.engine.getLifeExpectancy(), this.engine.getAverageCountOfChildren());
            allStatistic.getChildren().add(1, lineChartPanel.lineChart);
            app.getChildren().add(2, allStatistic);

            if (animalToTrick != null) {
                app.getChildren().remove(0);
                buttons.getChildren().remove(5);
                getAnimalStat();
                buttons.getChildren().add(5, animalStat);
                app.getChildren().add(0, buttons);
            }

            if (this.engine.isMagicDone()) {
                app.getChildren().remove(0);
                buttons.getChildren().remove(4);
                getAnimalStat();
                buttons.getChildren().add(4, new Label(magicAnnouncement + this.map.countWhichSpell));
                app.getChildren().add(0, buttons);
            }
        });

        if(!this.engine.isFinishSimulation() && this.engine.getDayOfSimulation()%FREQUENCY_OF_SAVING_STAT==0) {
            this.statisticWriter.saveStatisticToFile("src/main/resources/statistic.csv");
        } else if (this.engine.isFinishSimulation()){
            this.statisticWriter.addEndStatistic("src/main/resources/statistic.csv");
        }
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Get data");

        Label widthLabel = new Label("width: ");
        Label heightLabel = new Label("height");
        Label startEnergyLabel = new Label("start energy: ");
        Label moveEnergyLabel = new Label("move energy: ");
        Label plantEnergyLabel = new Label("plant energy: ");
        Label jungleInPercentLabel = new Label("jungle area[%]");
        Label chooseVersion = new Label("select one of version");

        TextField widthTextField = new TextField("15");
        TextField heightTextField = new TextField("15");
        TextField startEnergyTextField = new TextField("100");
        TextField moveEnergyTextField = new TextField("2");
        TextField plantEnergyTextField = new TextField("20");
        TextField jungleInPercentTextField = new TextField("15");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add("Normal");
        choiceBox.getItems().add("Magic");
        choiceBox.setValue("Normal");

        Button save = new Button("Save");
        save.setOnAction(e -> {
            width = isInt(widthTextField.getText());
            height = isInt(heightTextField.getText());
            startEnergy = isInt(startEnergyTextField.getText());
            moveEnergy = isInt(moveEnergyTextField.getText());
            plantEnergy = isInt(plantEnergyTextField.getText());
            jungleInPercent = isInt(jungleInPercentTextField.getText());
            jungleWidth = (int)(Math.sqrt(width*height*jungleInPercent/100));
            jungleHeight =  (int)(Math.sqrt(width*height*jungleInPercent/100));
            isNormalVersion = (choiceBox.getValue().equals("Normal"));

            Stage secondStage = new Stage();

            try {
                make();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            this.app = makeHBox();
            this.app.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
            this.app.setPadding(new Insets(20, 20, 20, 20));
            primaryStage.close();


            Scene scene = new Scene(app, 1200, 600);
            secondStage.setTitle("Darwin Evaluation");
            secondStage.setScene(scene);
            secondStage.show();
            secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    engine.finishSimulation();
                    Platform.exit();
                    System.exit(0);
                }
            });

        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(widthLabel, widthTextField,
                heightLabel, heightTextField,
                startEnergyLabel, startEnergyTextField,
                moveEnergyLabel, moveEnergyTextField,
                plantEnergyLabel, plantEnergyTextField,
                jungleInPercentLabel,jungleInPercentTextField,
                chooseVersion, choiceBox,
                save);

        scene = new Scene(layout, 300, 600);
        window.setScene(scene);
        window.show();
    }


    private void getAnimalStat() {
        if (animalToTrick != null) animalStat = new VBox(20, new Label(animalToTrick.toStringEt()));
    }

    private Integer isInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    private HBox makeHBox() {
        this.buttons = makeButton();
        this.buttons.setAlignment(Pos.BASELINE_LEFT);
        makeGrid();
        makeStatistics();
        lineChartPanel = new LineChartPanel();
        this.gridPane.setAlignment(Pos.TOP_CENTER);
        allStatistic.getChildren().add(lineChartPanel.lineChart);
        return new HBox(10, this.buttons, this.gridPane, this.allStatistic);
    }


    private VBox makeButton() {
        start = new Button("Start evauloation");
        start.setOnAction(e -> {
            if (!threadEngine.isAlive()) {
                this.engine.resumeSimulation();
                threadEngine.start();
            } else if(this.engine.isStopSimulation()){
                this.engine.resumeSimulation();
            }
        });


        stop = new Button("Stop era");
        stop.setOnAction(e -> {
            if(!this.engine.isStopSimulation()) this.engine.stopSimulation();
        });


        saveStatistics = new Button("Save Statistics");
        saveStatistics.setOnAction(e->{
            this.engine.stopSimulation();
            TextField textStatistic = new TextField("src/main/resources/statistic.csv");
            Label info = new Label("Save statistic to: ");
            Button ok = new Button("OK");
            ok.setOnAction(actionEvent -> {
                this.statisticWriter.saveStatisticToFile(textStatistic.getText());
                thirdStage.close();
            });
            VBox statisticVBox = new VBox(20);
            statisticVBox.getChildren().addAll(info,textStatistic,ok);
            Scene thirdScene = new Scene(statisticVBox, 400, 200);
            thirdStage = new Stage();
            thirdStage.setTitle("Info");
            thirdStage.setScene(thirdScene);
            thirdStage.show();
        });

        showDominateGenom = new Button("Show Dominate Genom: ");
        showDominateGenom.setOnAction(e->showAllAnimalsWithDominantGenes());


        buttons = new VBox(20);
        buttons.setMinSize(200, 50);
        buttons.getChildren().addAll(start, stop, saveStatistics, showDominateGenom, new Label(""), animalStat);
        return buttons;
    }

    private void makeGrid() {
        gridPane.setGridLinesVisible(true);
        this.startRowIndex = 0;
        this.startColumnIndex = 0;
        this.endRowIndex = width - 1;
        this.endColumnIndex = height - 1;

        for (int i = startRowIndex; i <= endRowIndex; i++) {
            Label label2 = new Label("");
            gridPane.add(label2, i, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(30));
        }

        for (int i = endColumnIndex; i >= startColumnIndex; i--) {
            Label label2 = new Label("");
            gridPane.add(label2, 0, i);
            gridPane.getRowConstraints().add(new RowConstraints(30));
        }

        fillGap();
    }

    private void fillGap() {
        for (int i = this.map.lowerLeftForJungle.x; i <= this.map.upperRightForJungle.x; i++) {
            for (int j = this.map.lowerLeftForJungle.y; j <= this.map.upperRightForJungle.y; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.DARKGREEN);
                rectangle.setWidth(30);
                rectangle.setHeight(30);
                gridPane.add(rectangle, i, j);
            }
        }

        for (Animal animal : this.map.listOfAnimal) {
            if (this.map.objectAt(animal.getPosition()) == animal) {
                IMapElement iMapElement = animal;
                GuiElementBox guiElementBox = new GuiElementBox(iMapElement, this);
                gridPane.add(guiElementBox.getAnimalImage(), animal.getPosition().x, animal.getPosition().y);
            }
        }

        for (Vector2d vector : this.map.tuftGrass.keySet()) {
            if (this.map.objectAt(vector) instanceof Grass) {
                IMapElement iMapElement = this.map.objectAt(vector);
                GuiElementBox guiElementBox = new GuiElementBox(iMapElement, this);
                gridPane.add(guiElementBox.getGrassImage(), vector.x, vector.y);
            }
        }

    }


    private void makeStatistics() {
        statistic = new Label("Day of simulation: \n" + this.engine.getDayOfSimulation() + "\n" +
                "All animals: " + this.map.listOfAnimal.size() + "\n" +
                "Dominant genotypes: \n" + Arrays.toString(this.engine.getDominateGenes()) + "\n" +
                "Average energy level for alive animals: " + this.engine.getAverageEnergyForAliveAnimals() + "\n" +
                "Life expectancy of animals for dead animals: " + this.engine.getLifeExpectancy() + "\n" +
                "The average number of children for alive animals: " + this.engine.getAverageCountOfChildren());
        setPadding(statistic);
        allStatistic.getChildren().add(0, statistic);
    }

    private void showAllAnimalsWithDominantGenes(){
        int[] dominantGenes = this.engine.getDominateGenes();

        for (Animal animal : this.map.listOfAnimal) {
            if (animal.getGenes().equals(dominantGenes)){
                Label dominant =  new Label("  !");
                dominant.setFont(Font.font(25));
                gridPane.add(dominant,animal.getPosition().x,animal.getPosition().y);
            }
        }
    }

    private Label setPadding(Label label) {
        label.setFont(Font.font(15));
        label.setFont(Font.font("Arial"));
        label.setPadding(new Insets(10, 10, 10, 10));
        return label;
    }


    public void mouseClickAnimals(Animal element) {
        Button showGenes = new Button("Show genes this animal");
        showGenes.setOnAction(e -> {
            if (this.animalStat != null) this.buttons.getChildren().remove(this.animalStat);
            Label stringAnimalStat = new Label("Genes of pointed element: ");
            this.animalStat = new VBox(20, stringAnimalStat, new Label(Arrays.toString(element.getGenes())));
            this.buttons.getChildren().add(this.animalStat);
            thirdStage.close();
        });

        Button startTracking = new Button("Start tracking");
        startTracking.setOnAction(e -> {
            element.setTricking(true);
            animalToTrick = element;
            if (this.animalStat != null) this.buttons.getChildren().remove(5);
            getAnimalStat();
            this.buttons.getChildren().add(5, this.animalStat);
            thirdStage.close();
        });

        VBox whatToDo = new VBox(20, showGenes, startTracking);
        whatToDo.setAlignment(Pos.CENTER);
        whatToDo.setPadding(new Insets(20, 20, 20, 20));
        Scene thirdScene = new Scene(whatToDo, 400, 200);
        thirdStage = new Stage();
        thirdStage.setTitle("Tell me what to do :)");
        thirdStage.setScene(thirdScene);
        thirdStage.show();
    }

}


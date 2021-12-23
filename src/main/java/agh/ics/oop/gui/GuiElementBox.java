package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.IMapElement;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class GuiElementBox extends ImageView {
    Rectangle myRectangle;
    Button but;

    protected IMapElement element;
    App app;

    public GuiElementBox(IMapElement element, App app) {
        this.app = app;
        this.element = element;

        if (element instanceof Grass) {
            this.myRectangle = new Rectangle(30, 30);
            myRectangle.setFill(Color.web(this.element.getColor()));

        } else {
            this.but = new Button();
            but.setStyle("-fx-background-color:" + this.element.getColor());
            but.setMaxSize(30, 30);
            but.setMinSize(30, 30);
            but.setOnAction(e -> {
                this.app.mouseClickAnimals((Animal) element);
            });
        }
    }

    public Rectangle getGrassImage() {
        return this.myRectangle;
    }

    public Button getAnimalImage() {
        return this.but;
    }
}

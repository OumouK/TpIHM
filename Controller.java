package projet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Cursor;

public class Controller {
    @FXML
    private RadioButton select;
    @FXML
    private RadioButton ellipse;
    @FXML
    private RadioButton rect;
    @FXML
    private RadioButton line;
    @FXML
    private Button btnClone;
    @FXML
    private Button btnDelete;
    @FXML
    private Pane canvas;
    @FXML
    private ColorPicker color;

    private int  i=0;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;

    //Pour stoquer les figures créés.
    public static List<Shape> figures= new ArrayList<>();

    //Différentes fonctions qui créent les figures.
    public Rectangle add_rec(double x, double y){
        Rectangle rec= new Rectangle(80,30);
        rec.setX(x);
        rec.setY(y);
        rec.setFill(color.getValue());
        rec.setCursor(Cursor.HAND);
        rec.setOnMousePressed(recOnMousePressedEventHandler);
        rec.setOnMouseDragged(recOnMouseDraggedEventHandler);
        canvas.getChildren().add(rec);
        return rec;
    }
    public Ellipse add_el(double x, double y){
        Ellipse e= new Ellipse(30,20);
        e.setCenterX(x);
        e.setCenterY(y);
        e.setFill(color.getValue());
        e.setCursor(Cursor.HAND);
        e.setOnMousePressed(eOnMousePressedEventHandler);
        e.setOnMouseDragged(eOnMouseDraggedEventHandler);
        canvas.getChildren().add(e);
        return e;
    }
    public Rectangle add_line(double x, double y){
        Rectangle l= new Rectangle(100,5);
        l.setX(x);
        l.setY(y);
        l.setFill(color.getValue());
        l.setCursor(Cursor.HAND);
        l.setOnMousePressed(recOnMousePressedEventHandler);
        l.setOnMouseDragged(recOnMouseDraggedEventHandler);
        canvas.getChildren().add(l);
        return l;
    }

    //Déclaration des differents handlers pour les differentes figures.
    EventHandler<MouseEvent> recOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Rectangle)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Rectangle)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> recOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((Rectangle)(t.getSource())).setTranslateX(newTranslateX);
                    ((Rectangle)(t.getSource())).setTranslateY(newTranslateY);
                }
            };

    EventHandler<MouseEvent> eOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Ellipse)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Ellipse)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> eOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;
                    ((Ellipse)(t.getSource())).setTranslateX(newTranslateX);
                    ((Ellipse)(t.getSource())).setTranslateY(newTranslateY);
                }
            };

    @FXML
    public void initialize(){
        //ToggleGroup pour la gestion des boutons radios, pour qu'un seul soit actif à la fois.
        ToggleGroup tog= new ToggleGroup();
        select.setToggleGroup(tog);
        ellipse.setToggleGroup(tog);
        rect.setToggleGroup(tog);
        line.setToggleGroup(tog);
        btnClone.setDisable(true);
        btnDelete.setDisable(true);
        tog.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue.equals(select)){
                    btnClone.setDisable(false);
                    btnDelete.setDisable(false);
                }
                else{
               btnClone.setDisable(true);
               btnDelete.setDisable(true);
           }
        }
        });

        //Gestion des events pour les differents boutons.
        //Petit bug pour les mouvements qui sont actifs même si le select/move n'est pas coché. J'ai pas eu le temps de le réglé :(.
        select.setOnAction(event -> {
          i = 1;
         for(Shape a:figures){
             a.setOnMouseClicked(event1 -> {
                  btnDelete.setOnAction(event2 -> {
                       canvas.getChildren().remove(a);
                       figures.remove(a);
                       });
                   btnClone.setOnAction(event3 ->{
                       if(a instanceof Rectangle){
                           Rectangle r= new Rectangle(((Rectangle) a).getWidth(), ((Rectangle) a).getHeight());
                           r.setX(((Rectangle) a).getX()+ 20);
                           r.setY(((Rectangle) a).getY()+ 20);
                           r.setFill(a.getFill());
                           r.setCursor(Cursor.HAND);
                           r.setOnMousePressed(recOnMousePressedEventHandler);
                           r.setOnMouseDragged(recOnMouseDraggedEventHandler);
                           canvas.getChildren().add(r);
                           figures.add(r);
                           i=0;
                           btnDelete.setOnAction(event2 -> {
                               figures.remove(r);
                               canvas.getChildren().remove(r);

                           });
                       }
                       else if(a instanceof Ellipse){
                           Ellipse e= new Ellipse(((Ellipse) a).getRadiusX(), ((Ellipse) a).getRadiusY());
                           e.setCenterX(((Ellipse) a).getCenterX()+20);
                           e.setCenterY(((Ellipse) a).getCenterY()+20);
                           e.setFill(a.getFill());
                           e.setCursor(Cursor.HAND);
                           e.setOnMousePressed(eOnMousePressedEventHandler);
                           e.setOnMouseDragged(eOnMouseDraggedEventHandler);
                           canvas.getChildren().add(e);
                           figures.add(e);
                           btnDelete.setOnAction(event2 -> {
                               canvas.getChildren().remove(e);
                               figures.remove(e);
                           });
                          }
                    });
               });
           }
       });

        ellipse.setOnAction(event -> {
                i=2;
        });

        rect.setOnAction(event -> {
            i=3;
        });

        line.setOnAction(event -> {
                i = 4;
        });

       //Gestion des evenements du canvas.
      canvas.setOnMouseClicked(event -> {
            switch (i){
                case 1:
                    break;
                case 2:
                    Ellipse e=add_el(event.getX(), event.getY());
                    figures.add(e);
                    break;
                case 3:
                    Rectangle r= add_rec(event.getX(), event.getY());
                    figures.add(r);
                    break;
                case 4:
                    Rectangle l= add_line(event.getX(), event.getY());
                    figures.add(l);
                    break;
                default:
                    break;
            }
        });
    }
    }

package application;

import agent.Status;
import world.Planet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import agent.State;
import world.Type;

import java.util.Timer;
import java.util.TimerTask;


public class App extends Application {


    protected GridPane grid = new GridPane();
    protected ImageView[] roverIcons = new ImageView[Planet.nbagents];
    protected ImageView[] hsroverIcons = new ImageView[Planet.nbagents];
    protected ImageView[] rechargeroverIcons = new ImageView[Planet.nbagents];
    Color colNight = new Color(0.25,0,0.9,1.0);
    Color colDay = new Color(0.82,0.26,0.07,1.0);
    public boolean currentDayLightState = Planet.dayLight;
    public int currentNumberOfSamples = getNumberOfSample();
    public Timer timer;
    public int timePerFrame = 10;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {

            //@Override
            public void run() {
                Platform.runLater(() -> {
                    updateUI();
                });
            }

        }, 0, timePerFrame);
    }



    public void initUI(Stage primaryStage) {
        primaryStage.setTitle("Planet");
        HBox hbox = new HBox();

        drawGround();
        drawAgents();

        hbox.getChildren().add(grid);
        Scene scene = new Scene(hbox,Planet.WIDTH,Planet.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateUI() {

        updateSamples();
        updateAgents();
        updateLight();


    }

    public int getNumberOfSample() {
        int res = 0;
        for (int i=0 ; i<Planet.SIZE ; i++) {
            for (int j = 0; j < Planet.SIZE; j++) {
                if (Planet.terrain.getType(i, j).equals(Type.SAMPLE)) {
                    res++;
                }
            }
        }
        return res;
    }

    public void updateSamples() {

        int numberOfSample = getNumberOfSample();
        if (numberOfSample < currentNumberOfSamples) {
            currentNumberOfSamples = numberOfSample;
            grid.getChildren().removeAll();
            drawGround();
            drawAgents();
        }
    }


    public void updateAgents() {

        for (int i=0 ; i<Planet.nbagents ; i++) {
            State state = Planet.states.get(i);
            int x = state.getX();
            int y = state.getY();
            grid.getChildren().remove(roverIcons[i]);
            grid.getChildren().remove(hsroverIcons[i]);
            grid.getChildren().remove(rechargeroverIcons[i]);

            Status status = Planet.states.get(i).getStatus();
            if (status.equals(Status.RUNNING)) {
                grid.add(roverIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.HS)) {
                grid.add(hsroverIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.RECHARGING)) {
                grid.add(rechargeroverIcons[i], y, x+1, 1,1);
            }
//            if (Planet.states.get(i).isHS()) {
//                grid.add(hsroverIcons[i], y, x+1, 1,1);
//            }
//            else {
//                grid.add(roverIcons[i], y, x+1, 1,1);
//            }
        }
    }

    public void updateLight() {

        if (Planet.dayLight != currentDayLightState) {
            currentDayLightState = Planet.dayLight;
            grid.getChildren().removeAll();
            drawGround();
            drawAgents();
        }
    }

    public void drawAgents() {
        int len_square = Planet.WIDTH / Planet.SIZE;

        for (int i=0 ; i<Planet.nbagents ; i++) {
            if (Planet.nbagents <= 6) {
                roverIcons[i] = new ImageView("assets/rover" + i + ".png");
                hsroverIcons[i] = new ImageView("assets/hsrover" + i + ".png");
                rechargeroverIcons[i] = new ImageView("assets/rechargerover" + i + ".png");
            }
            else {
                roverIcons[i] = new ImageView("assets/rover0.png");
                hsroverIcons[i] = new ImageView("assets/hsrover0.png");
                rechargeroverIcons[i] = new ImageView("assets/rechargerover0.png");
            }
            roverIcons[i].setPreserveRatio(true);
            roverIcons[i].setFitWidth(len_square);
            roverIcons[i].setFitHeight(len_square);

            hsroverIcons[i].setPreserveRatio(true);
            hsroverIcons[i].setFitWidth(len_square);
            hsroverIcons[i].setFitHeight(len_square);

            rechargeroverIcons[i].setPreserveRatio(true);
            rechargeroverIcons[i].setFitWidth(len_square);
            rechargeroverIcons[i].setFitHeight(len_square);

            int x = Planet.states.get(i).getX();
            int y = Planet.states.get(i).getY();

            Status status = Planet.states.get(i).getStatus();
            if (status.equals(Status.RUNNING)) {
                grid.add(roverIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.HS)) {
                grid.add(hsroverIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.RECHARGING)) {
                grid.add(rechargeroverIcons[i], y, x+1, 1,1);
            }
//            if (Planet.states.get(i).isHS()) {
//                grid.add(hsroverIcons[i], y, x+1, 1,1);
//            }
//            else {
//                grid.add(roverIcons[i], y, x+1, 1,1);
//            }
        }
    }

    public void drawGround() {
        int len_square = Planet.WIDTH / Planet.SIZE;
        Color col;
        if (Planet.dayLight) {
            col = colDay;
        }
        else {
            col = colNight;
        }
        for (int i=0 ; i<Planet.SIZE ; i++) {
            for (int j=0 ; j<Planet.SIZE ; j++) {
                Rectangle rec = new Rectangle(len_square,len_square);
                rec.setFill(col);
                grid.add(rec, j, i+1, 1, 1);
                if (Planet.terrain.getType(i,j) == Type.CRATER) {
                    ImageView fissure = new ImageView("assets/fissure3.png");
                    fissure.setPreserveRatio(true);
                    fissure.setFitWidth(len_square);
                    fissure.setFitHeight(len_square);
                    grid.add(fissure, j, i+1, 1,1);
                }
                if (Planet.terrain.getType(i,j) == Type.SAMPLE) {
                    ImageView sample = new ImageView("assets/sample.png");
                    sample.setPreserveRatio(true);
                    sample.setFitWidth(len_square/2.0);
                    sample.setFitHeight(len_square/2.0);
                    grid.add(sample, j, i+1, 1,1);
                }
            }
        }
    }
}

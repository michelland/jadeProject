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
    protected ImageView[] exploringIcons = new ImageView[Planet.nbagents];
    protected ImageView[] hsIcons = new ImageView[Planet.nbagents];
    protected ImageView[] rechargingIcons = new ImageView[Planet.nbagents];
    protected ImageView[] savingIcons = new ImageView[Planet.nbagents];
    protected ImageView[] analysingIcons = new ImageView[Planet.nbagents];
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
            grid.getChildren().remove(exploringIcons[i]);
            grid.getChildren().remove(hsIcons[i]);
            grid.getChildren().remove(rechargingIcons[i]);
            grid.getChildren().remove(savingIcons[i]);
            grid.getChildren().remove(analysingIcons[i]);

            Status status = Planet.states.get(i).getStatus();
            if (status.equals(Status.RUNNING)) {
                grid.add(exploringIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.HS)) {
                grid.add(hsIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.RECHARGING)) {
                grid.add(rechargingIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.SAVING)) {
                grid.add(savingIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.ANALYSING)) {
                grid.add(analysingIcons[i], y, x+1,1,1);
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
                exploringIcons[i] = new ImageView("assets/exploring/rover" + i + ".png");
                hsIcons[i] = new ImageView("assets/hs/rover" + i + ".png");
                rechargingIcons[i] = new ImageView("assets/recharging/rover" + i + ".png");
                savingIcons[i] = new ImageView("assets/saving/rover" + i + ".png");
                analysingIcons[i] = new ImageView("assets/analysing/rover" + i + ".png");
            }
            else {
                exploringIcons[i] = new ImageView("assets/exploring/rover0.png");
                hsIcons[i] = new ImageView("assets/hs/rover0.png");
                rechargingIcons[i] = new ImageView("assets/recharging/rover0.png");
                savingIcons[i] = new ImageView("assets/saving/rover0.png");
                analysingIcons[i] = new ImageView("assets/analysing/rover0.png");
            }
            exploringIcons[i].setPreserveRatio(true);
            exploringIcons[i].setFitWidth(len_square);
            exploringIcons[i].setFitHeight(len_square);

            hsIcons[i].setPreserveRatio(true);
            hsIcons[i].setFitWidth(len_square);
            hsIcons[i].setFitHeight(len_square);

            rechargingIcons[i].setPreserveRatio(true);
            rechargingIcons[i].setFitWidth(len_square);
            rechargingIcons[i].setFitHeight(len_square);

            savingIcons[i].setPreserveRatio(true);
            savingIcons[i].setFitWidth(len_square);
            savingIcons[i].setFitHeight(len_square);

            analysingIcons[i].setPreserveRatio(true);
            analysingIcons[i].setFitWidth(len_square);
            analysingIcons[i].setFitHeight(len_square);



            int x = Planet.states.get(i).getX();
            int y = Planet.states.get(i).getY();

            Status status = Planet.states.get(i).getStatus();
            if (status.equals(Status.RUNNING)) {
                grid.add(exploringIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.HS)) {
                grid.add(hsIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.RECHARGING)) {
                grid.add(rechargingIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.SAVING)) {
                grid.add(savingIcons[i], y, x+1, 1,1);
            }
            else if (status.equals(Status.ANALYSING)) {
                grid.add(analysingIcons[i], y, x+1, 1,1);
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

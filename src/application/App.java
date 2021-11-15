package application;

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
import world.Type;

import java.util.Timer;
import java.util.TimerTask;


public class App extends Application {


    protected GridPane grid = new GridPane();
    protected ImageView roverIcon;
    public Timer timer;
    public int timePerFrame = 100;

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
        primaryStage.setTitle("App");
        HBox hbox = new HBox();

        drawGround();
        drawAgents();

        hbox.getChildren().add(grid);
        Scene scene = new Scene(hbox,Planet.WIDTH,Planet.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateUI() {

        refreshAgents();
    }


    public void refreshAgents() {
        int x = Planet.position.getX();
        int y = Planet.position.getY();
        grid.getChildren().remove(roverIcon);
        grid.add(roverIcon, y, x+1,1,1);
    }

    public void drawAgents() {
        int len_square = Planet.WIDTH / Planet.SIZE;
        int x = Planet.position.getX();
        int y = Planet.position.getY();
        roverIcon = new ImageView("assets/rover.png");
        roverIcon.setPreserveRatio(true);
        roverIcon.setFitWidth(len_square/2);
        roverIcon.setFitHeight(len_square/2);
        grid.add(roverIcon, y, x+1, 1,1);
    }

    public void drawGround() {
        int len_square = Planet.WIDTH / Planet.SIZE;
        Color col = new Color(0.82,0.26,0.07,1.0);
        for (int i=0 ; i<Planet.SIZE ; i++) {
            for (int j=0 ; j<Planet.SIZE ; j++) {
                Rectangle rec = new Rectangle(len_square,len_square);
                rec.setFill(col);
                grid.add(rec, j, i+1, 1, 1);
                if (Planet.terrain.getType(i,j) == Type.CRATER) {
                    ImageView fissure = new ImageView("assets/fissure.png");
                    fissure.setPreserveRatio(true);
                    fissure.setFitWidth(len_square);
                    fissure.setFitHeight(len_square);
                    grid.add(fissure, j, i+1, 1,1);
                }
                if (Planet.terrain.getType(i,j) == Type.SAMPLE) {
                    ImageView sample = new ImageView("assets/sample.png");
                    sample.setPreserveRatio(true);
                    sample.setFitWidth(len_square/2);
                    sample.setFitHeight(len_square/2);
                    grid.add(sample, j, i+1, 1,1);
                }
            }
        }
    }
}

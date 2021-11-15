package UI;

import application.Planet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import world.Position;
import world.Terrain;
import world.Type;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class App extends Application {

    protected int WIDTH;
    protected int HEIGHT;
    protected int SIZE;
    protected Planet planet;
    protected Terrain terrain;
    protected Position pos;
    protected GridPane grid = new GridPane();
    protected int position;

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static App application = null;

    public Timer timer;
    public int timePerFrame = 1000;

    public static App waitForApp() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return application;
    }

    public static void setUpApp(App startUpTest0) {
        application = startUpTest0;
        latch.countDown();
    }

    public App() {
        setUpApp(this);
    }


    public void setupUI() {

        terrain = Planet.getTerrain();
        WIDTH = Planet.WIDTH;
        HEIGHT = Planet.HEIGHT;
        SIZE = Planet.SIZE;
        pos = Planet.position;
        System.out.println(terrain.toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
//
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

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void initUI(Stage primaryStage) {
        primaryStage.setTitle("App");
        HBox hbox = new HBox();


        drawGround();

        hbox.getChildren().add(grid);
        Scene scene = new Scene(hbox,WIDTH,HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateUI() {

        drawGround();
    }

    public void addPosition(int sum) {
        position += sum;
    }

    public void drawGround() {
        int len_square = WIDTH / SIZE;
        Color col = new Color(0.82,0.26,0.07,1.0);
        int color = 0;
        for (int i=0 ; i<SIZE ; i++) {
            color = (color + 1) % 2;
            int tmp = color;
            for (int j=0 ; j<SIZE ; j++) {
                tmp = (tmp + 1) % 2;
                Rectangle rec = new Rectangle(len_square,len_square);
                if (tmp == 0) {
                    rec.setFill(col);
                }
                else {
                    rec.setFill(col);
                }
                grid.add(rec, j, i+1, 1, 1);
                if (terrain.getType(i,j) == Type.CRATER) {
                    ImageView fissure = new ImageView("assets/fissure.png");
                    fissure.setPreserveRatio(true);
                    fissure.setFitWidth(len_square);
                    fissure.setFitHeight(len_square);
                    grid.add(fissure, j, i+1, 1,1);
                }
                if (terrain.getType(i,j) == Type.SAMPLE) {
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

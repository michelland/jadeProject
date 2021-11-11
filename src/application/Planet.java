package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import world.Terrain;

import jade.core.Profile;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import world.Type;

public class Planet extends Application {

    /************************* PARAMATERS ***************************/
    public static final int SIZE = 10;
    public static final float CRATER_RATE = 0.1f;
    public static final float SAMPLE_RATE = 0.3f;

    /*************************** WORLD *****************************/
    public static Terrain terrain;

    /**************************** UI *****************************/
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public GridPane grid;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
    }

    public void initUI(Stage primaryStage) {
        primaryStage.setTitle("Planet");

        Label label1 = new Label(terrain.toString());
        Button button1 = new Button("click me");
        HBox hbox = new HBox();
        grid = new GridPane();

        drawGround();

        hbox.getChildren().add(grid);
        Scene scene = new Scene(hbox,WIDTH,HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void InitTerrain() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);
        System.out.println(terrain.toString());
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


    public static void main(String[] args) {
        try {
            InitTerrain();

            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(properties);
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            AgentController agent1=mainContainer.createNewAgent("Astero", "agent.Rover", new Object[]{});
            AgentController agent2=mainContainer.createNewAgent("Rivero", "agent.Rover", new Object[]{});
            agent1.start();
            agent2.start();
            Application.launch(Planet.class);
        } catch (ControllerException e) {
            e.printStackTrace();
        }



    }



}

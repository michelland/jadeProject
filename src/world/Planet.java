package world;

import UI.App;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import world.Position;
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

public class Planet extends Agent {

    /************************* PARAMATERS ***************************/
    public static final int SIZE = 10;
    public static final float CRATER_RATE = 0.1f;
    public static final float SAMPLE_RATE = 0.3f;

    /*************************** WORLD *****************************/
    public static Terrain terrain;
    public static Position position = new Position(14,24);
    public static int i = 0;

    /**************************** UI *****************************/
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    @Override
    protected void setup() {
        InitTerrain();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(myAgent.getLocalName() + ":" + msg.getContent());
                    if (msg.getContent().equals("10")) {
                        System.out.println("update terrain");
                        terrain.setType(Type.CRATER, 1,0);
                        terrain.setType(Type.CRATER, 2,0);
                        terrain.setType(Type.CRATER, 3,0);
                        terrain.setType(Type.CRATER, 4,0);
                        terrain.setType(Type.CRATER, 5,0);
                    }
                }

                block();
            }
        });
    }

    public static Terrain getTerrain() {
        return terrain;
    }

    public static void InitTerrain() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);
        System.out.println("terrain created");
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de la planète");
    }
}

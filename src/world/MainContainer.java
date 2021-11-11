package world;

import jade.core.Profile;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.core.ProfileImpl;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.core.ContainerID;
import jade.security.JADEPrincipal;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import agent.Rover;

public class MainContainer {

    public static final int SIZE = 5;
    public static final float CRATER_RATE = 0.1f;
    public static final float SAMPLE_RATE = 0.3f;


    public static Terrain terrain;

    public static void InitWorld() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);
        System.out.println(terrain.toString());
    }




    public static void main(String[] args) {
        try {
            InitWorld();

            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(properties);
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            AgentController agentController=mainContainer.createNewAgent("Astero", "agent.Rover", new Object[]{});
            agentController.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }



    }



}

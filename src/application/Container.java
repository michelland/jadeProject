package application;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import world.Planet;

public class Container {

    public static void main(String[] args) {
        try {

            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(properties);
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            AgentController planet = mainContainer.createNewAgent("Planet", "world.Planet", new Object[]{});
            for (int i=0; i<Planet.nbagents; i++) {
                mainContainer.createNewAgent(String.valueOf(i), "agent.Rover", new Object[]{});
            }
            planet.start();
            for (int i =0; i<Planet.nbagents ; i++) {
                mainContainer.getAgent(Integer.toString(i)).start();
            }

            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(App.class);
                }
            }.start();

        } catch (ControllerException e) {
            e.printStackTrace();
        }



    }
}

package application;

import UI.App;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import world.Type;

public class Container {

    public static void main(String[] args) {
        try {

            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(properties);
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            AgentController planet = mainContainer.createNewAgent("Planet", "world.Planet", new Object[]{});
            AgentController agent1=mainContainer.createNewAgent("Astero", "agent.Rover", new Object[]{});
            AgentController agent2=mainContainer.createNewAgent("Rivero", "agent.Rover", new Object[]{});
            planet.start();
            agent1.start();
            agent2.start();


            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(App.class);
                }
            }.start();
            App application = App.waitForApp();
            application.setupUI();

        } catch (ControllerException e) {
            e.printStackTrace();
        }



    }
}

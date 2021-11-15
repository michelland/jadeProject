package world;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class Planet extends Agent {

    /************************* PARAMATERS ***************************/
    public static final int SIZE = 10;
    public static final float CRATER_RATE = 0.1f;
    public static final float SAMPLE_RATE = 0.3f;

    /*************************** WORLD *****************************/
    public static Terrain terrain;
    public static Position position = new Position(0,0);

    /**************************** UI *****************************/
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    @Override
    protected void setup() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] coordinates = msg.getContent().split(",");
                    position.setX(Integer.parseInt(coordinates[0]));
                    position.setY(Integer.parseInt(coordinates[1]));
                    System.out.println(myAgent.getLocalName() +
                            " > Rover a la position " + position.getX() + "," + position.getY());
                }
                block();
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de la planète");
    }
}

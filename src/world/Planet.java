package world;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;


public class Planet extends Agent {

    /************************* PARAMATERS ***************************/
    public static final int SIZE = 10;
    public static final float CRATER_RATE = 0.1f;
    public static final float SAMPLE_RATE = 0.3f;

    /*************************** WORLD *****************************/
    public static Terrain terrain;
    public static int nbagents = 5;
    public static State state = new State(0,0);
    public static Map<Integer,State> states = new HashMap<Integer, State>();

    /**************************** UI *****************************/
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    @Override
    protected void setup() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);
        for (int i=0 ; i<nbagents ; i++) {
            states.put(i, new State(0,0));
        }

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    int sender = Integer.parseInt(msg.getSender().getLocalName());
                    String[] coordinates = msg.getContent().split(",");
                    states.get(sender).setX(Integer.parseInt(coordinates[0]));
                    states.get(sender).setY(Integer.parseInt(coordinates[1]));
                    System.out.println(myAgent.getLocalName() +
                            " > Rover " + sender + " a la position " + states.get(sender).getX() + "," + states.get(sender).getY());
                }
                //block();
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de la planète");
    }
}

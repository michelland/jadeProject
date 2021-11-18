package world;

import agent.State;
import agent.Status;
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
    public static boolean dayLight = false;
    public static int heure = 0;
    public static int nbagents = 6;
    public static State state = new State(0,0);
    public static Map<Integer, State> states = new HashMap<Integer, State>();

    public static int time = 0;

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
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
                if (time % 100 == 0) {
                    dayLight = !dayLight;
                }
                //block();
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    int sender = Integer.parseInt(msg.getSender().getLocalName());
                    String[] message = msg.getContent().split(":");
                    String type = message[0];
                    String content = message[1];
                    switch (type) {
                        case "position":
                            String[] coordinates = content.split(",");
                            states.get(sender).setX(Integer.parseInt(coordinates[0]));
                            states.get(sender).setY(Integer.parseInt(coordinates[1]));
                            //System.out.println(myAgent.getLocalName() +
                            //        " > Rover " + sender + " a la position " + states.get(sender).getX() + "," + states.get(sender).getY());
                            break;
                        case "hs":
                            states.get(sender).setHS(true);
                            states.get(sender).setStatus(Status.HS);
                            break;
                    }
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

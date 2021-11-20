package world;

import agent.Rover;
import agent.State;
import agent.Status;
import bdi.Decision_Process;
import bdi.Filter;
import bdi.Plans;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;


public class Planet extends Agent {

    /************************* PARAMATERS ***************************/
    public static final int SIZE = 10;
    public static final float CRATER_RATE = 0.05f;
    public static final float SAMPLE_RATE = 0.3f;

    /*************************** WORLD *****************************/
    public static Terrain terrain;
    public static boolean dayLight = true;
    public static int heure = 0;
    public static int heureNuit = 18;
    public static int heureJour = 0;
    public static int timerTick = 1000;
    public static int nbagents = 3;

    /**************************** UI *****************************/
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;

    /**************************** AGENT *****************************/
    public static final int rechargeEfficiency = 80;
    public static final int dechargeEfficiency = 10;
    public static final int numberOfSampleNecessaryForAnalysis = 4;
    public static final int gatherVariance = 3;
    public static Map<Integer, State> states = new HashMap<Integer, State>();

    @Override
    protected void setup() {
        terrain = new Terrain(SIZE, CRATER_RATE, SAMPLE_RATE);
        for (int i=0 ; i<nbagents ; i++) {
            states.put(i, new State(0,0));
        }

        addBehaviour(new TickerBehaviour(this, timerTick) {
            @Override
            public void onTick() {
                heure = (heure + 1) % 24;
                if (heure == heureNuit) {
                    dayLight = false;
                }
                else if (heure == heureJour){
                    dayLight = true;
                }
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
                            states.get(sender).setStatus(Status.HS);
                            break;
                        case "running":
                            states.get(sender).setStatus(Status.RUNNING);
                            break;
                        case "recharging":
                            states.get(sender).setStatus(Status.RECHARGING);
                            break;
                        case "saving":
                            states.get(sender).setStatus(Status.SAVING);
                            break;
                        case "analysing":
                            states.get(sender).setStatus(Status.ANALYSING);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de la planète");
    }
}

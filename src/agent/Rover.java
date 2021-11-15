package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;
import world.Planet;
import agent.Position;
import agent.State;
import world.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Rover extends Agent {

    protected String name;
    protected State state;

    @Override
    protected void setup() {
        name = this.getAID().getLocalName();
        int x = (int) (Math.random() * (Planet.SIZE-5));
        int y = (int) (Math.random() * (Planet.SIZE-5));
        state = new State(x,y);
        state.setStatus(Status.RUNNING);

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveRandom();
                System.out.println(name + " > je suis a la position " + state.getX() + "," + state.getY());
                sendPositionToPlanet();
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(myAgent.getLocalName() + ":" + msg.getContent());
                }
                //block();
            }
        });

    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de " + name);
    }

    protected void sendMessage(String content, String dest) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(content);
        message.addReceiver(new AID(dest, AID.ISLOCALNAME));
        send(message);
    }

    public void sendPositionToPlanet() {
        String content = "position:" + state.getX() + "," + state.getY();
        sendMessage(content, "Planet");
    }

    public void sendHSToPlanet() {
        String content = "hs: ";
        sendMessage(content, "Planet");
    }

    public void moveRandom() {
        if (!state.isHS()) {
            Position current = new Position(state.getX(),state.getY());
            Vector<Position> possible_positions = current.legalPositions();
            int index = (int) (Math.random() * (possible_positions.size()));
            state.setX(possible_positions.get(index).x);
            state.setY(possible_positions.get(index).y);
            if (Planet.terrain.getType(state.getX(),state.getY()) == Type.CRATER) {
                state.setHS(true);
                state.setStatus(Status.HS);
                sendHSToPlanet();
            }
        }
    }
}

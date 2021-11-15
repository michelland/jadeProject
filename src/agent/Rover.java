package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import world.Position;

public class Rover extends Agent {

    protected String name;
    protected int positionX;
    protected int positionY;

    @Override
    protected void setup() {
        name = this.getAID().getLocalName();
        positionX = 1;
        positionY = 1;

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                positionX++;
                positionY++;
                System.out.println(name + " > je suis a la position " + positionX + "," + positionY);
                sendPositionToPlanet();
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(myAgent.getLocalName() + ":" + msg.getContent());
                }
                block();
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
        String content = "" + positionX + "," + positionY;
        sendMessage(content, "Planet");
    }
}

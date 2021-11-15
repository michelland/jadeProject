package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Rover extends Agent {

    protected String name;
    protected int positionX;
    protected int positionY;

    protected int i = 0;

    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
    }

    public String getNickname() {
        return name;
    }

    @Override
    protected void setup() {
        name = this.getAID().getLocalName();
        positionX = 0;
        positionY = 1;

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                String content = Integer.toString(i);
                i++;
                sendMessage(content);
                try {
                    Thread.sleep(1000);
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

    protected void sendMessage(String content) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(content);
        message.addReceiver(new AID("Planet", AID.ISLOCALNAME));
        send(message);
    }
}

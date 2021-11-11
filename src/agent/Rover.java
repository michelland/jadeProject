package agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Rover extends Agent {

    protected String name;


    public String getNickname() {
        return name;
    }

    @Override
    protected void setup() {
        name = this.getAID().getLocalName();
        sendMessage(this.getLocalName());
        System.out.println("Je suis " + name);
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de " + name);
    }

    protected void sendMessage(String mess) {
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        inform.setContent(mess);
        inform.setProtocol("information");
    }
}

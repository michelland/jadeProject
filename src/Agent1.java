import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Agent1 extends Agent {

    protected String name;

    public Agent1(String _name) {
        super();
        name = _name;
    }

    protected void setup() {
        sendMessage(this.getLocalName());
        System.out.println(name);

    }

    protected void sendMessage(String mess) {
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        inform.setContent(mess);
        inform.setProtocol("information");
    }
}

package agent;

import desire.Explore;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import world.Planet;
import world.Type;

import java.util.Vector;


public class Rover extends Agent {

    protected String name;
    //public State state;
    public ACLMessage msg;
    public int battery_pourcentage = 100;
    public int nb_sample = 0;
    public int heure = 0;
    protected Beliefs beliefs;
    protected FSMBehaviour fsm;

    public Beliefs getBeliefs() { return beliefs;}
    public ACLMessage getMsg() { return beliefs.getMsg();}
    public int getBattery_pourcentage() { return beliefs.getBattery_pourcentage();}
    public int getNb_sample() { return beliefs.getNb_sample();}
    public int getY() { return beliefs.getY();}
    public int getX() { return beliefs.getX();}
    public int getHeure() { return heure;}
    public Status getStatus() { return beliefs.getStatus();}
    public boolean isHS() { return getStatus()==Status.HS;}
    public void setX(int x) { beliefs.setX(x);}
    public void setY(int y) { beliefs.setY(y);}
    public void setStatus(Status status) { beliefs.setStatus(status);}
    public void setHS(boolean b) { beliefs.setHs();}

    @Override
    protected void setup() {
        int x = (int) (Math.random() * (Planet.SIZE));
        int y = (int) (Math.random() * (Planet.SIZE));
        beliefs = new Beliefs(this.getAID().getLocalName(),x,y);



        /*addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveRandom();


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
        });*/

        FSMBehaviour fsm = new FSMBehaviour(this);
        fsm.registerFirstState(new Explore(this),"exploring");

        fsm.registerTransition("exploring", "exploring", 0);


    }
    private class startBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            System.out.println("c'est parti");
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de " + name);
    }

    public void sendMessage(String content, String dest) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(content);
        message.addReceiver(new AID(dest, AID.ISLOCALNAME));
        send(message);
    }

    public void sendPositionToPlanet() {
        String content = "position:" + state.getX() + "," + state.getY();
        sendMessage(content, "Planet");
        System.out.println(name + " > je suis a la position " + state.getX() + "," + state.getY());
    }

    public void sendHSToPlanet() {
        String content = "hs: ";
        sendMessage(content, "Planet");
        System.out.println(name + " > je suis HS");
    }

    public void moveRandom() {
        if (!state.isHS()) {
            Position current = new Position(state.getX(),state.getY());
            Vector<Position> possible_positions = current.legalPositions();
            int index = (int) (Math.random() * (possible_positions.size()));
            state.setX(possible_positions.get(index).x);
            state.setY(possible_positions.get(index).y);
            sendPositionToPlanet();
            if (Planet.terrain.getType(state.getX(),state.getY()) == Type.CRATER) {
                state.setHS(true);
                state.setStatus(Status.HS);
                sendHSToPlanet();
            }
        }
    }
    private static class receiving extends OneShotBehaviour{

        Rover agent;

        public receiving(Rover _a){
            this.agent = _a;
        }

        @Override
        public void action() {
            agent.msg = agent.receive();
        }
    }




}



package agent;

import desire.Explore;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import world.Planet;
import world.Type;
import bdi.Beliefs;

import java.util.Vector;


public class Rover extends Agent {

    protected Beliefs beliefs;
    protected FSMBehaviour fsm;

    public Beliefs getBeliefs() { return beliefs;}
    public ACLMessage getMsg() { return beliefs.getMsg();}
    public int getBattery_pourcentage() { return beliefs.getBattery_pourcentage();}
    public int getNb_sample() { return beliefs.getNb_sample();}
    public int getY() { return beliefs.getY();}
    public int getX() { return beliefs.getX();}
    public int getHeure() { return  beliefs.getHeure();}
    public Status getStatus() { return beliefs.getStatus();}
    public boolean isHS() { return getStatus()==Status.HS;}
    public void setX(int x) { beliefs.setX(x);}
    public void setY(int y) { beliefs.setY(y);}
    public void setMsg(ACLMessage mess) {beliefs.setMsg(mess);}
    public void setStatus(Status status) { beliefs.setStatus(status);}

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
        // update beliefs
        // compute next intentions
        //compue the desir
        //filter
        //      if same desire do intentions
        //      else compute intention from blank intention
        //give intention to plans
        //plans do the action need for the intention


    }
    private class startBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            System.out.println("c'est parti");
        }
    }

    public void perception() {
        beliefs.setCurrentType(Planet.terrain.getType(getX(),getY()));
        ACLMessage msg = receive();
        if (msg != null) {
            setMsg(msg);
            System.out.println(getName() + ":" + msg.getContent());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de " + getName());
    }

    public void sendMessage(String content, String dest) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(content);
        message.addReceiver(new AID(dest, AID.ISLOCALNAME));
        send(message);
    }

    public void sendPositionToPlanet() {
        String content = "position:" + getX() + "," + getY();
        sendMessage(content, "Planet");
        System.out.println(getName() + " > je suis a la position " + getX() + "," + getY());
    }

    public void sendHSToPlanet() {
        String content = "hs: ";
        sendMessage(content, "Planet");
        System.out.println(getName() + " > je suis HS");
    }

    public void moveRandom() {
        if (!isHS()) {
            Position current = new Position(getX(),getY());
            Vector<Position> possible_positions = current.legalPositions();
            int index = (int) (Math.random() * (possible_positions.size()));
            setX(possible_positions.get(index).x);
            setY(possible_positions.get(index).y);
            sendPositionToPlanet();
            if (Planet.terrain.getType(getX(),getY()) == Type.CRATER) {
                setStatus(Status.HS);
                sendHSToPlanet();
            }
        }
    }
}



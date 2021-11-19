package agent;

import bdi.*;
import com.sun.xml.internal.bind.v2.TODO;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import world.Planet;
import world.Type;

import java.util.Vector;


public class Rover extends Agent {

    protected Beliefs beliefs;
    protected Intention intention;
    protected Desire desire;

    protected boolean helpSended = false;

    public Beliefs getBeliefs() {return beliefs;}
    public ACLMessage getMsg() {return beliefs.getMsg();}
    public int getBattery_pourcentage() {return beliefs.getBattery_pourcentage();}
    public int getNb_sample() {return beliefs.getNb_sample();}
    public int getY() {return beliefs.getY();}
    public int getX() {return beliefs.getX();}
    public int getHeure() {return beliefs.getHeure();}
    public Type getCurrentType() {return beliefs.getCurrentType();}
    public Status getStatus() {return beliefs.getStatus();}
    public boolean isHS() {return getStatus() == Status.HS;}
    public void setX(int x) {beliefs.setX(x);}
    public void setY(int y) {beliefs.setY(y);}
    public void setMsg(ACLMessage mess) {beliefs.setMsg(mess);}
    public void setStatus(Status status) {beliefs.setStatus(status);}
    public void setHeure(int h) {beliefs.setHeure(h);}
    public boolean nextToMayday() {return beliefs.nextToMayday();}
    public int getX_mayday() {return beliefs.getX_mayday();}
    public int getY_mayday() {return beliefs.getY_mayday();}

    @Override
    protected void setup() {
        int x = (int) (Math.random() * (Planet.SIZE));
        int y = (int) (Math.random() * (Planet.SIZE));
        beliefs = new Beliefs(getLocalName(), x, y);
        desire = Desire.PROGRESS;
        intention = Intention.EXPLORING;

        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            public void onTick() {

//                System.out.println(getX_mayday());
//                System.out.println(getY_mayday());
                //System.out.println(beliefs.getIdMayday());
                perception();
                intention = Decision_Process.option(desire, intention, (Rover) this.myAgent);
                desire = Decision_Process.des(desire, intention, (Rover) this.myAgent);
                intention = Filter.filter(desire, intention, (Rover) this.myAgent);
                Plans.execute(intention, (Rover) this.myAgent);
                //block();
            }
        });
        // update beliefs
        // compute next intentions
        //compue the desir
        //filter
        //      if same desire do intentions
        //      else compute intention from blank intention
        //give intention to plans
        //plans do the action need for the intention


    }
    //TODO ecrire fonction qui reçoit message, et l'appeler au début de perception
    public void receiveMayday() {
        ACLMessage msg = receive();
        if (msg != null) {
            String message = msg.getContent();
            String[] parsed_message = message.split("-");
            String type = parsed_message[0];
            String content = parsed_message[1];
            if (type.equals("mayday")) {
                String[] coordinates = content.split(",");
                beliefs.setIdMayday(msg.getSender().getLocalName());
                beliefs.setX_mayday(Integer.parseInt(coordinates[0]));
                beliefs.setY_mayday(Integer.parseInt(coordinates[1]));
            }
            else if (type.equals("save")) {
                beliefs.setStatus(Status.RUNNING);
                helpSended = false;
                sendStatusToPlanet();
            }
        }
    }

    public void perception() {
        beliefs.setCurrentType(Planet.terrain.getType(getX(), getY()));
        receiveMayday();
        setHeure(Planet.heure);
    }

    @Override
    protected void takeDown() {
        System.out.println("Destruction de " + getName());
    }

    public void sendMessage(int type, String content, String dest) {
        ACLMessage message = new ACLMessage(type);
        message.setContent(content);
        message.addReceiver(new AID(dest, AID.ISLOCALNAME));
        send(message);
    }

    public void sendMayday() {
        if (!helpSended) {
            helpSended = true;
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            String content = "mayday-" + getX() + "," + getY();
            for (int i=0 ; i<Planet.nbagents ; i++) {
                if (i != Integer.parseInt(getLocalName())) {
                    sendMessage(ACLMessage.REQUEST, content, Integer.toString(i));
                    System.out.println(getLocalName() + " > " + i + " " + content);
                }
            }
        }
    }


    public void sendPositionToPlanet() {
        String content = "position:" + getX() + "," + getY();
        sendMessage(ACLMessage.INFORM, content, "Planet");
        //System.out.println(getName() + " > je suis a la position " + getX() + "," + getY());
    }

    public void sendStatusToPlanet() {
        String content = "";
        if (beliefs.getStatus() == Status.HS) {
            content = "hs: ";
        }
        else if (beliefs.getStatus() == Status.RUNNING){
            content = "running: ";
        }
        else if (beliefs.getStatus() == Status.RECHARGING) {
            content = "recharching: ";
        }
        sendMessage(ACLMessage.INFORM, content, "Planet");
        //System.out.println(getName() + " > je suis HS");
    }

    public void moveRandom() {
        Position current = new Position(getX(), getY());
        Vector<Position> possible_positions = current.legalPositions();
        int index = (int) (Math.random() * (possible_positions.size()));
        move(possible_positions.get(index).x, possible_positions.get(index).y);
    }

    public void move(int _x, int _y) {
        if (!isHS() && batteryRemaining()) {
            setX(_x);
            setY(_y);
            sendPositionToPlanet();
            if (Planet.terrain.getType(getX(), getY()) == Type.CRATER) {
                setStatus(Status.HS);
                sendStatusToPlanet();
            }
            decharge();
        }
    }


    public boolean batteryRemaining() {
        return getBattery_pourcentage() >= 10;
    }

    public void recharge() {
        if (Planet.dayLight) {
            int percentage = beliefs.getBattery_pourcentage();
            if (percentage < 100) {
                int min = 1;
                int max = Planet.rechargeEfficiency / 10;
                int recharge_rate = (int) ((Math.random() * (max - min + 1)) + min);
                beliefs.setBattery_pourcentage((percentage + (recharge_rate * 10)) % 100);
                System.out.println(getLocalName() + " > " + " recharging " + (percentage + (recharge_rate * 10)) % 100);
            }
        }
    }

    public void decharge() {
        if (batteryRemaining()) {
            int percentage = beliefs.getBattery_pourcentage();
            beliefs.setBattery_pourcentage((percentage - Planet.dechargeEfficiency));
        }
    }


    public void gather() {
        if (batteryRemaining()) {
            if (Planet.terrain.getType(getX(), getY()).equals(Type.SAMPLE)) {
                int sample_size = (int) ((Math.random() * (Planet.gatherVariance - 1)) + 1);
                beliefs.setNb_sample(getNb_sample() + sample_size);
                Planet.terrain.removeSample(getX(), getY());
            }
            decharge();
        }
    }

    public void analysing() {
        if (batteryRemaining()) {
            int number_of_sample = beliefs.getNb_sample();
            if (number_of_sample >= Planet.numberOfSampleNecessaryForAnalysis) {
                beliefs.setNb_sample(number_of_sample - Planet.numberOfSampleNecessaryForAnalysis);
                System.out.println("Analyse réussit");
            } else {
                System.out.println("Analyse échouée");
            }
            decharge();
        }
    }

    public void save() {
        if (batteryRemaining() && nextToMayday()) {
            String content = "save- ";
            sendMessage(ACLMessage.INFORM, content, beliefs.getIdMayday());
            beliefs.setX_mayday(-1);
            beliefs.setY_mayday(-1);
            System.out.println(getLocalName() + " > " + beliefs.getIdMayday() + " : " + "I'm saving you !");
        }
    }

    public void moveToMayday() {
        System.out.println("helping " + getX_mayday() + "," + getY_mayday());
        int diffX = getX() - getBeliefs().getX_mayday();
        int diffY = getY() - getBeliefs().getY_mayday();

        if (diffX < 0) {
            move(getX() + 1, getY());
        }
        else if (diffX > 0) {
            move(getX() - 1, getY());
        }
        else if (diffY < 0) {
            move(getX(), getY() + 1);
        }
        else if (diffY > 0) {
            move(getX(), getY() - 1);
        }
        decharge();
    }

}
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
    protected void takeDown() {
        System.out.println("Destruction de " + getName());
    }
    public boolean batteryRemaining() {return getBattery_pourcentage() >= 10;}

    @Override
    protected void setup() {
        int x = (int) (Math.random() * (Planet.SIZE));
        int y = (int) (Math.random() * (Planet.SIZE));
        beliefs = new Beliefs(getLocalName(), x, y);
        desire = Desire.PROGRESS;
        intention = Intention.EXPLORING;

        addBehaviour(new TickerBehaviour(this, Planet.timerTick) {
            @Override
            public void onTick() {
                perception();
                intention = Decision_Process.option(desire, intention, (Rover) this.myAgent);
                desire = Decision_Process.des(desire, intention, (Rover) this.myAgent);
                intention = Filter.filter(desire, intention, (Rover) this.myAgent);
                Plans.execute(intention, (Rover) this.myAgent);
            }
        });
    }

    /**************************************** MESSAGES ********************************************/
    public void sendMessage(int type, String content, String dest) {
        ACLMessage message = new ACLMessage(type);
        message.setContent(content);
        message.addReceiver(new AID(dest, AID.ISLOCALNAME));
        send(message);
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
            content = "recharging: ";
        }
        else if (beliefs.getStatus() == Status.SAVING) {
            content = "saving: ";
        }
        else if (beliefs.getStatus() == Status.ANALYSING) {
            content = "analysing: ";
        }
        sendMessage(ACLMessage.INFORM, content, "Planet");
        //System.out.println(getName() + " > je suis HS");
    }

    /**************************************** MOVEMENT ********************************************/
    public void decharge() {
        if (batteryRemaining()) {
            int percentage = beliefs.getBattery_pourcentage();
            beliefs.setBattery_pourcentage((percentage - Planet.dechargeEfficiency));
        }
    }

    public void move(int _x, int _y) {
        if (!isHS() && batteryRemaining()) {
            setX(_x);
            setY(_y);
            //sendPositionToPlanet();
            if (Planet.terrain.getType(getX(), getY()) == Type.CRATER) {
                setStatus(Status.HS);
                //sendStatusToPlanet();
            }
            decharge();
        }
    }

    public void addPositionToTabou(int x, int y) {
        Position p = new Position(x,y);
        if (!p.isIn(beliefs.getTabou())) {
            beliefs.getTabou().add(p);
        }
    }

    public void addCurrentPositionToVisited() {
        Position p = new Position(beliefs.getX(), beliefs.getY());
        if (!p.isIn(beliefs.getVisited())) {
            beliefs.getVisited().add(p);
        }
    }

    /************************************** PERCEPTION ***************************************/
    public void perception() {
        beliefs.setCurrentType(Planet.terrain.getType(getX(), getY()));
        setHeure(Planet.heure);
        receiveMessage();
        sendPositionToPlanet();
        sendStatusToPlanet();
    }

    public void receiveMessage() {
        ACLMessage msg = receive();
        if (msg != null) {
            String message = msg.getContent();
            String[] parsed_message = message.split("-");
            String type = parsed_message[0];
            String content = parsed_message[1];
            if (type.equals("mayday")) {
                String[] coordinates = content.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                addPositionToTabou(x,y);
                beliefs.setIdMayday(msg.getSender().getLocalName());
                beliefs.setX_mayday(x);
                beliefs.setY_mayday(y);
            }
            else if (type.equals("save")) {
                //System.out.println(getLocalName() + " > " + "Je suis sauf");
                beliefs.setStatus(Status.RUNNING);
                beliefs.setMaydaySendedTimer(0);
                beliefs.setIdHelping("");
                sendStatusToPlanet();
            }
            else if (type.equals("helping")) {
                if (beliefs.getIdHelping().equals("")) {
                    beliefs.setIdHelping(msg.getSender().getLocalName());
                    sendNoMayday();
                }
            }
            else if (type.equals("nomayday")) {
                beliefs.setX_mayday(-1);
                beliefs.setY_mayday(-1);
            }
        }
    }



    /************************************** EXPLORING ***************************************/
    public void moveRandom() {
        beliefs.setStatus(Status.RUNNING);
        Position current = new Position(getX(), getY());
        addCurrentPositionToVisited();
        Vector<Position> legal_positions = current.legalPositions();
        Vector<Position> safe_positions = current.safePositions(beliefs.getTabou());
        Vector<Position> good_positions = current.goodPositions(safe_positions, beliefs.getVisited());
        int index;
        if (!good_positions.isEmpty()) {
            index = (int) (Math.random() * (good_positions.size()));
            move(good_positions.get(index).x, good_positions.get(index).y);
        }
        else if (!safe_positions.isEmpty()) {
            index = (int) (Math.random() * (safe_positions.size()));
            move(safe_positions.get(index).x, safe_positions.get(index).y);
        }
        else {
            index = (int) (Math.random() * (legal_positions.size()));
            move(legal_positions.get(index).x, legal_positions.get(index).y);
        }
    }

    /************************************** GATHERING ****************************************/
    public void gather() {
        beliefs.setStatus(Status.RUNNING);
        if (batteryRemaining()) {
            if (Planet.terrain.getType(getX(), getY()).equals(Type.SAMPLE)) {
                int sample_size = (int) ((Math.random() * (Planet.gatherVariance - 1)) + 1);
                beliefs.setNb_sample(getNb_sample() + sample_size);
                Planet.terrain.removeSample(getX(), getY());
            }
            decharge();
        }
    }

    /************************************** ANALYSING ****************************************/
    public void analysing() {
        beliefs.setStatus(Status.ANALYSING);
        if (batteryRemaining()) {
            int number_of_sample = beliefs.getNb_sample();
            if (number_of_sample >= Planet.numberOfSampleNecessaryForAnalysis) {
                beliefs.setNb_sample(number_of_sample - Planet.numberOfSampleNecessaryForAnalysis);
                //System.out.println(getLocalName() + " > " + "Analyse compl??te !");
            } else {
                //System.out.println(getLocalName() + " > " + "Analyse ??chou??e");
            }
            decharge();
        }
    }

    /************************************** RECHARGING ***************************************/
    public void recharge() {

        beliefs.setStatus(Status.RECHARGING);
        if (Planet.dayLight) {
            int percentage = beliefs.getBattery_pourcentage();
            if (percentage < 100) {
                int min = 1;
                int max = Planet.rechargeEfficiency / 10;
                int recharge_rate = (int) ((Math.random() * (max - min + 1)) + min);
                beliefs.setBattery_pourcentage((percentage + (recharge_rate * 10)) % 100);
            }
        }
    }

    /************************************** REACHING *****************************************/
    public void moveToMayday() {
        beliefs.setStatus(Status.RUNNING);
        if (!beliefs.isHelpingSended()) {
            String content = "helping- ";
            sendMessage(ACLMessage.INFORM, content, beliefs.getIdMayday());
            System.out.println(getLocalName() + " > " + beliefs.getIdMayday() + " " + content);
            beliefs.setHelpingSended(true);
        }
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

    /************************************** SAVING *******************************************/
    public void save() {
        beliefs.setStatus(Status.SAVING);
        if (batteryRemaining() && nextToMayday()) {
            beliefs.setHelpingSended(true);
            String content = "save- ";
            sendMessage(ACLMessage.INFORM, content, beliefs.getIdMayday());
            beliefs.setX_mayday(-1);
            beliefs.setY_mayday(-1);
            System.out.println(getLocalName() + " > " + beliefs.getIdMayday() + " " + content);
        }
    }

    /************************************** MAYDAY *******************************************/

    public void sendMayday() {
        addPositionToTabou(beliefs.getX(),beliefs.getY());
        if ((beliefs.getMaydaySendedTimer() % 10) == 0) {
            String content = "mayday-" + getX() + "," + getY();
            for (int i=0 ; i<Planet.nbagents ; i++) {
                if (i != Integer.parseInt(getLocalName())) {
                    sendMessage(ACLMessage.REQUEST, content, Integer.toString(i));
                    System.out.println(getLocalName() + " > " + i + " " + content);
                }
            }
        }
        beliefs.setMaydaySendedTimer(beliefs.getMaydaySendedTimer() + 1);
    }

    public void sendNoMayday() {
        String content = "nomayday-" + getX() + "," + getY();
        for (int i=0 ; i<Planet.nbagents ; i++) {
                if ((i != Integer.parseInt(getLocalName())) && (i != Integer.parseInt(beliefs.getIdHelping()))){
                    sendMessage(ACLMessage.INFORM, content, Integer.toString(i));
                    System.out.println(getLocalName() + " > " + i + " " + content);
                }
        }
    }
}


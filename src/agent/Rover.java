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
    public State state;
    public ACLMessage msg;
    public int battery_pourcentage = 100;
    public int nb_sample = 0;
    public int heure = 0;



    @Override
    protected void setup() {
        name = this.getAID().getLocalName();
        int x = (int) (Math.random() * (Planet.SIZE));
        int y = (int) (Math.random() * (Planet.SIZE));
        state = new State(x,y);


        state.setStatus(Status.RUNNING);

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

    protected void sendMessage(String content, String dest) {
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


    private class exploring extends OneShotBehaviour{

        Rover rover;
        int next_state = 0;

        public exploring(Rover _rover) {
            rover = _rover;
        }

        @Override
        public void action() {
            String content_msg = "getinfo:" + rover.state.getX() + "," + rover.state.getY();
            sendMessage(content_msg, "Planet");

            rover.msg = rover.blockingReceive();

            String[] content = rover.msg.getContent().split("-");
            if(rover.battery_pourcentage < 10){
                next_state = 1; // go to recharging
            }
            else if(Objects.equals(content[0], "help")){
                next_state = 2; // go to help
                //TODO add the parse of a position and the varaible in rover to stotre this position
            }
            else if(Objects.equals(content[0], "sample")){
                next_state = 3; // go to gathering
            }
            else if(Objects.equals(content[0], "mouvement")){
                //TODO add the code to choose in what direction go
                //binary number on each direction summed to have a int as a sup up of direction
                // 1    or perhaps juste a list inside []
                //8 2
                // 4
                next_state = 0; // redo this state
            }
        }

        public int onEnd(){
            return next_state;
        }
    }

}



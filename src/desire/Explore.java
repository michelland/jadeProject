package desire;

import agent.Position;
import agent.Rover;
import agent.Status;
import jade.core.behaviours.OneShotBehaviour;
import world.Planet;
import world.Type;

import java.util.Objects;
import java.util.Vector;

//public class Explore extends OneShotBehaviour{
//
//    Rover rover;
//    int next_state = 0;
//
//    public Explore(Rover _rover) {
//        rover = _rover;
//    }
//
//    @Override
//    public void action() {
//        String content_msg = "getinfo:" + rover.getX() + "," + rover.getY();
//        rover.sendMessage(content_msg, "Planet");
//
//        rover.msg = rover.blockingReceive();
//
//        String[] content = rover.msg.getContent().split("-");
//        if(rover.battery_pourcentage < 10){
//            next_state = 1; // go to recharging
//        }
//        else if(Objects.equals(content[0], "help")){
//            next_state = 2; // go to help
//            //TODO add the parse of a position and the varaible in rover to stotre this position
//        }
//        else if(Objects.equals(content[0], "sample")){
//            next_state = 3; // go to gathering
//        }
//        else{
//            if (!rover.isHS()) {
//                Position current = new Position(rover.getX(),rover.getY());
//                Vector<Position> possible_positions = current.legalPositions();
//                int index = (int) (Math.random() * (possible_positions.size()));
//                rover.setX(possible_positions.get(index).x);
//                rover.setY(possible_positions.get(index).y);
//                rover.sendPositionToPlanet();
//                if (Planet.terrain.getType(rover.getX(),rover.getY()) == Type.CRATER) {
//                    rover.setHS(true);
//                    rover.setStatus(Status.HS);
//                    rover.sendHSToPlanet();
//                }
//            }
//            next_state = 0; // redo this state
//        }
//    }
//
//    public int onEnd(){
//        return next_state;
//    }
//}

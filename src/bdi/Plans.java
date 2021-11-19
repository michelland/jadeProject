package bdi;

import agent.Rover;

public class Plans {

    public void Plans(){
    }

    public static void execute(Intention intention, Rover rover) {

            switch (intention) {
                case GATHERING:
                    System.out.println(rover.getLocalName() + " > GATHERING, I have " + rover.getNb_sample() + " samples");
                    rover.gather();
                    break;
                case EXPLORING:
                    //System.out.println(rover.getLocalName() + " > EXPLORING");
                    rover.moveRandom();
                    break;
                case ANALYSING:
                    System.out.println(rover.getLocalName() + " > ANALYSING");
                    rover.analysing();
                    break;
                case RECHARGING:
                    System.out.println(rover.getLocalName() + " > RECHARGING");
                    rover.recharge();
                    break;
                case REACHING:
                    //System.out.println(rover.getLocalName() + " > REACHING");
                    rover.moveToMayday();
                    break;
                case SAVING:
                    System.out.println(rover.getLocalName() + " > SAVING");
                    rover.save();
                    break;
                case MAYDAY:
                    //System.out.println(rover.getLocalName() + " > MAYDAY");
                    rover.sendMayday();
                    break;
            }
    }

}

package bdi;

import agent.Rover;

public class Plans {

    public void Plans(){
    }

    public static void execute(Intention intention, Rover rover) {

            switch (intention) {
                case GATHERING:
                    rover.gather();
                    System.out.println(rover.getLocalName() + " > GATHERING, I have " + rover.getNb_sample() + " samples");
                    break;
                case EXPLORING:
                    rover.moveRandom();
                    System.out.println(rover.getLocalName() + " > EXPLORING");
                    break;
                case ANALYSING:
                    rover.analysing();
                    System.out.println(rover.getLocalName() + " > ANALYSING");
                    break;
                case RECHARGING:
                    rover.recharge();
                    System.out.println(rover.getLocalName() + " > RECHARGING");
                    break;
                case REACHING:
                    rover.moveToMayday();
                    System.out.println(rover.getLocalName() + " > REACHING");
                    break;
                case SAVING:
                    rover.save();
                    System.out.println(rover.getLocalName() + " > SAVING");
                    break;
                case MAYDAY:
                    rover.sendMayday();
                    System.out.println(rover.getLocalName() + " > MAYDAY");
                    break;
            }
    }

}

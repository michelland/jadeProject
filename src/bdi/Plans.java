package bdi;

import agent.Rover;

public class Plans {

    public void Plans(){
    }

    public static void execute(Intention intention, Rover rover) {

            switch (intention) {
                case GATHERING:
                    rover.gather();
                    System.out.println(rover.getName() + " > GATHERING, I have " + rover.getNb_sample() + " samples");
                    break;
                case EXPLORING:
                    rover.moveRandom();
                    System.out.println(rover.getName() + " > EXPLORING");
                    break;
                case ANALYSING:
                    rover.analysing();
                    System.out.println(rover.getName() + " > ANALYSING");
                    break;
                case RECHARGING:
                    rover.recharge();
                    System.out.println(rover.getName() + " > RECHARGING");
                    break;
                case REACHING:
                    rover.moveToMayday();
                    System.out.println(rover.getName() + " > REACHING");
                    break;
                case SAVING:
                    //rover.gather();
                    System.out.println(rover.getName() + " > SAVING");
                    break;
                case MAYDAY:
                    //rover.gather();
                    System.out.println(rover.getName() + " > MAYDAY");
                    break;
            }
    }

}

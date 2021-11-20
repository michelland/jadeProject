package bdi;

import agent.Rover;
import world.Type;

public class Decision_Process {

    public static void Decision_Process(){
    }

    public static Intention option(Desire _d, Intention _i, Rover _r){
        boolean cond1 = _r.getBattery_pourcentage() <= 10;
        boolean cond2 = _r.getBattery_pourcentage() <= _r.getBeliefs().getGreedLevel();
        boolean cond3 = (_r.getBeliefs().getTimeBeforeNight() <= 2) && (_r.getBeliefs().getTimeBeforeNight() >= 1);
        if(_d == Desire.PROGRESS){
            if(_r.isHS()){
                return Intention.MAYDAY;
            }
            if(cond1 || (cond2 && cond3)){
                return Intention.RECHARGING;
            }
            if(_r.getCurrentType() == Type.SAMPLE){
                return Intention.GATHERING;
            }
            if(_r.getNb_sample() >= 4){
                return Intention.ANALYSING;
            }
            else{
                return Intention.EXPLORING;
            }
        }
        else{
            if(_r.isHS()){
                return Intention.MAYDAY;
            }
            if(cond1 || (cond2 && cond3)){
                return Intention.RECHARGING;
            }
            if(_r.nextToMayday()){
                return Intention.SAVING;
            }
            else{
                return Intention.REACHING;
            }
        }
    }

    public static Desire des (Desire _d, Intention _i, Rover _r){
        if(_d == Desire.PROGRESS){
            if(_r.getX_mayday() == -1 && _r.getY_mayday() == -1){
                return Desire.PROGRESS;
            }
            else {
                return Desire.HELP;
            }
        }
        else{
            if(_r.getX_mayday() == -1 && _r.getY_mayday() == -1){
                return Desire.PROGRESS;
            }
            else{
                return Desire.HELP;
            }
        }

    }
}

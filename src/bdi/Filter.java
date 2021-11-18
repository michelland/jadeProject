package bdi;

import agent.Rover;

import static bdi.Decision_Process.option;
import static bdi.Decision_Process.des;

public class Filter {
    public void Filter(){

    }

    public Intention filter(Desire _d, Intention _i, Rover _r){
        Intention new_intention = option(_d, _i, _r);
        Desire new_desire = des(_d, _i, _r);

        if( _d == new_desire){
            return new_intention;
        }
        else{
            return option(new_desire, _i, _r);
        }
    }

}

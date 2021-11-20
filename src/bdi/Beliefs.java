package bdi;

import agent.Position;
import agent.Status;
import jade.lang.acl.ACLMessage;
import world.Planet;
import world.Type;

import java.util.Vector;

public class Beliefs {
    protected String name;
    protected ACLMessage msg;
    protected int battery_pourcentage = 100;
    protected int nb_sample = 0;
    protected int heure = 0;
    protected String idMayday;
    protected String idHelping = "";
    protected int x;
    protected int x_mayday = -1;
    protected int y;
    protected int y_mayday = -1;
    protected Vector<Position> tabou = new Vector<>();
    protected Vector<Position> visited = new Vector<>();
    protected Status status;
    protected Type currentType;
    protected int maydaySendedTimer = 0;
    protected boolean helpingSended = false;
    protected int greedLevel = Planet.greedLevel;

    public Beliefs(String name, int _x, int _y){
        setName(name);
        x = _x;
        y = _y;
        status = Status.RUNNING;
    }

    protected int getGreedLevel() {
        return greedLevel;
    }
    public Vector<Position> getVisited() {
        return visited;
    }
    public Vector<Position> getTabou() {
        return tabou;
    }

    public int getTimeBeforeNight() {
        if (Planet.heureNuit <= getHeure()) {
            return 0;
        }
        else {
            return Planet.heureNuit - getHeure();
        }
    }

    public boolean isHelpingSended() {
        return helpingSended;
    }

    public void setHelpingSended(boolean helpingSended) {
        this.helpingSended = helpingSended;
    }

    public int getMaydaySendedTimer() {
        return maydaySendedTimer;
    }

    public void setMaydaySendedTimer(int helpSended) {
        this.maydaySendedTimer = helpSended;
    }

    public void setIdHelping(String idHelping) {
        this.idHelping = idHelping;
    }

    public String getIdHelping() {
        return idHelping;
    }

    public void setIdMayday(String idMayday) {
        this.idMayday = idMayday;
    }

    public String getIdMayday() {
        return idMayday;
    }

    public void setCurrentType(Type currentType) {this.currentType = currentType;}
    public Type getCurrentType() {
            return currentType;
        }

    public String getName() {return name;}
    public ACLMessage getMsg() {
        return msg;
    }

    public int getBattery_pourcentage() {
        return battery_pourcentage;
    }

    public int getNb_sample() {
        return nb_sample;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX_mayday() { return x_mayday; }

    public int getY_mayday() { return y_mayday; }

    public void setY(int y) {
            this.y = y;
        }

    public int getX() {
        return x;
    }

    public void setX_mayday(int x_mayday) { this.x_mayday = x_mayday; }

    public void setY_mayday(int y_mayday) { this.y_mayday = y_mayday; }

    public Status getStatus() {
            return status;
        }

    public int getHeure() {
        return heure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(ACLMessage msg) {
        this.msg = msg;
    }

    public void setBattery_pourcentage(int battery_pourcentage) {
        this.battery_pourcentage = battery_pourcentage;
    }

    public void setNb_sample(int nb_sample) {
        this.nb_sample = nb_sample;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean nextToMayday(){
        return (x == x_mayday && y == y_mayday-1) ||
                (x == x_mayday && y == y_mayday+1) ||
                (x == x_mayday+1 && y == y_mayday) ||
                (x == x_mayday-1 && y == y_mayday);
    }

}

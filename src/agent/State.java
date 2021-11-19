package agent;

public class State {

    protected int x;
    protected int y;
    protected boolean hs;
    protected Status status;

    public State(int _x,int _y) {
        x = _x;
        y = _y;
        hs = false;
        status = Status.RUNNING;
    }

    public void setX(int _x) {
        x = _x;
    }
    public void setY(int _y) {
        y = _y;
    }
    public void setHS(boolean b) { hs = b;}
    public void setStatus(Status s) { status = s;}
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isHS() {
        return status==Status.HS;
    }
    public Status getStatus() { return status; }
}

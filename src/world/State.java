package world;

public class State {

    protected int x;
    protected int y;
    protected boolean hs;

    public State(int _x,int _y) {
        x = _x;
        y = _y;
        hs = false;
    }

    public void setX(int _x) {
        x = _x;
    }
    public void setY(int _y) {
        y = _y;
    }
    public void setHS(boolean b) { hs = b;}
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isHS() { return hs;}
}

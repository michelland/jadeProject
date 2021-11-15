package world;

import java.util.Vector;

public class Position {

    public int x;
    public int y;

    public Position(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public Position Right() {
        return new Position(x+1,y);
    }
    public Position Left() {
        return new Position(x-1,y);
    }
    public Position Up() {
        return new Position(x,y-1);
    }
    public Position Down() {
        return new Position(x,y+1);
    }

    public boolean isLegalPosition() {
        return (x>=0 & x<Planet.SIZE & y>=0 & y<Planet.SIZE);
    }

    public Vector<Position> legalPositions() {
        Vector<Position> res= new Vector<>();
        if (Up().isLegalPosition()) {res.add(Up());}
        if (Down().isLegalPosition()) {res.add(Down());}
        if (Left().isLegalPosition()) {res.add(Left());}
        if (Right().isLegalPosition()) {res.add(Right());}
        return res;
    }
}

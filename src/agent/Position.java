package agent;

import world.Planet;

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

    public boolean equals(Position p) {
        return ((x == p.x) && (y == p.y));
    }

    public boolean isIn(Vector<Position> positions) {
        for (Position p : positions) {
            if (this.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalPosition() {
        return (x>=0 & x< Planet.SIZE & y>=0 & y<Planet.SIZE);
    }

    public Vector<Position> goodPositions(Vector<Position> safe_positions, Vector<Position> visited) {
        Vector<Position> positions = safe_positions;
        positions.removeIf(p -> p.isIn(visited));
        return positions;
    }

    public Vector<Position> safePositions(Vector<Position> tabou) {
        Vector<Position> res= new Vector<>();
        Position up = Up();
        Position down = Down();
        Position left = Left();
        Position right = Right();
        if (up.isLegalPosition() && !up.isIn(tabou)) {
            res.add(Up());
        }
        if (down.isLegalPosition() && !down.isIn(tabou)) {
            res.add(Down());
        }
        if (left.isLegalPosition() && !left.isIn(tabou)) {
            res.add(Left());
        }
        if (right.isLegalPosition() && !right.isIn(tabou)) {
            res.add(Right());
        }
        return res;
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

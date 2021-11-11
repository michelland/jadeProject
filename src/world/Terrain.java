package world;

public class Terrain {
    protected Type[][] grid;
    protected int size;
    protected boolean daylight = true;

    public Terrain(int _size, float crater_rate, float sample_rate) {
        size = _size;
        grid = new Type[size][size];
        for(int x = 0 ; x<size; x++){
            for(int y = 0 ; y<size; y++){
                if( Math.random() <= crater_rate){
                    grid[x][y] = Type.CRATER;
                }
                else if( Math.random() <= sample_rate){
                    grid[x][y] = Type.SAMPLE;
                }
                else{
                    grid[x][y] = Type.EMPTY;
                }
            }
        }
    }

    public Type[][] getGrid() {
        return grid;
    }

    public Type getType(int i, int j) {
        return grid[i][j];
    }

    public void setType(Type type, int i, int j) {
        grid[i][j] = type;
    }

    public void removeSample(int x, int y) {
        assert getType(x,y) == Type.SAMPLE;
        setType(Type.EMPTY, x, y);

    }

    public String toString() {
        String mess = "[";
        for(int i=0 ; i<size;i++) {
            for(int j=0 ; j<size ; j++) {
                mess += getType(i,j).toString();
                mess += ",";
            }
            mess += "\n";
        }
        mess += "]";
        return mess;
    }



}

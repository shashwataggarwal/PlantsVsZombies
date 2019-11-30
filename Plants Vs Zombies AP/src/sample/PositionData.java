package sample;

import java.io.Serializable;

public abstract class PositionData implements Serializable {

    protected int x;
    protected int y;

    public PositionData(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

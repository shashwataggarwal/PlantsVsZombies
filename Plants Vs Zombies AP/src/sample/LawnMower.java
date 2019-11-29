package sample;

import javafx.scene.image.Image;

public class LawnMower extends Positionable{
    private final int speed;
    private boolean used;
    private final int row;
    public LawnMower(int row) {
        speed=4;
        used=false;
        this.row=row;
    }

    public boolean isUsed() {
        return used;
    }
    public void use() {
        used=true;
    }
    public int getRow() {
        return row;
    }

    @Override
    public Image getImage() {
        return null;
    }
}

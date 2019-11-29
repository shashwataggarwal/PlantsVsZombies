package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Bomb extends Plant {
    public Bomb(int health) {
        super(health);
    }
    public abstract int getBlastRadius();
    public void blast() {

    }
}

class CherryBomb extends Bomb {
    private static final int CHERRY_BLAST_RADIUS=2;
    private static final int CHERRY_COST=150;
    private static final int CHERRY_RECHARGE_TIME=10;
    private static final int CHERRY_HEALTH=10;
    private static Image image;
    public CherryBomb() {
        super(CHERRY_HEALTH);
    }

    @Override
    public Image getImage() {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/cherrybomb.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public int getBlastRadius() {
        return CHERRY_BLAST_RADIUS;
    }

    @Override
    public int getRechargeTime() {
        return CHERRY_RECHARGE_TIME;
    }

    @Override
    public int getCost() {
        return CHERRY_COST;
    }
}
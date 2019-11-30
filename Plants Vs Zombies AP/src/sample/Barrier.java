package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Barrier extends Plant {
    public Barrier(int health) {
        super(health);
    }
    public abstract int getInitialStopPower();
}

class Wallnut extends Barrier {
    private static final int WALLNUT_COST=50;
    private static final int WALLNUT_STOP_POWER=1000;
    private static final int WALLNUT_RECHARGE_TIME=15;
    private static Image image;
    public Wallnut() {
        super(WALLNUT_STOP_POWER);
    }

    @Override
    public Image getImage() {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/wallnut.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public int getInitialStopPower() {
        return WALLNUT_STOP_POWER;
    }

    @Override
    public int getRechargeTime() {
        return WALLNUT_RECHARGE_TIME;
    }

    @Override
    public int getCost() {
        return WALLNUT_COST;
    }
}


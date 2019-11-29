package sample;

import javafx.animation.Timeline;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class SunPlant extends Plant {
    public SunPlant(int health) {
        super(health);
    }
    public void produceSun() {

    }
    public abstract int getProductionTime();
}

class Sunflower extends SunPlant {
    private static final int SUNFLOWER_COST=50;
    private static final int SUNFLOWER_TIME=10;
    private static final int SUNFLOWER_RECHARGE_TIME=5;
    private static final int SUNFLOWER_HEALTH=100;
    private static Image image;
    private Timeline sunTimeline;
    public Sunflower() {
        super(SUNFLOWER_HEALTH);
    }


    @Override
    public Image getImage() {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/sunflower.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public void setSunTimeline(Timeline sunTimeline) {
        this.sunTimeline = sunTimeline;
    }

    public Timeline getSunTimeline() {
        return sunTimeline;
    }

    @Override
    public int getRechargeTime() {
        return SUNFLOWER_RECHARGE_TIME;
    }

    @Override
    public int getCost() {
        return SUNFLOWER_COST;
    }

    @Override
    public int getProductionTime() {
        return SUNFLOWER_TIME;
    }
}

package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class BoostedZombie extends Zombie{
    private static final int BOOSTEDZOMBIE_ATTACK_POWER = 8;
    private static final int BOOSTEDZOMBIE_DEFENSE_POWER = 3;
    private static final int BOOSTEDZOMBIE_SPEED = 2;
    private static final int BOOSTEDZOMBIE_HEALTH=30;
    private static Image image;

    public BoostedZombie(HashMap<Integer,Plant> p) {
        super(BOOSTEDZOMBIE_HEALTH,p);
        this.attackPower = BOOSTEDZOMBIE_ATTACK_POWER;
        this.defensePower = BOOSTEDZOMBIE_DEFENSE_POWER;
        this.speed = BOOSTEDZOMBIE_SPEED;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public Image getImage()  {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/boostedZombie.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

}

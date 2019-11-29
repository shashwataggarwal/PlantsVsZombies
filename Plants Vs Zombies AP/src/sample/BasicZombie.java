package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BasicZombie extends Zombie{
    private static final int BASICZOMBIE_ATTACK_POWER = 5;
    private static final int BASICZOMBIE_DEFENSE_POWER = 2;
    private static final int BASICZOMBIE_SPEED = 1;
    private static final int BASICZOMBIE_HEALTH=50;
    private static Image image;
    public BasicZombie() {
        super(BASICZOMBIE_HEALTH);
        this.attackPower = BASICZOMBIE_ATTACK_POWER;
        this.defensePower = BASICZOMBIE_DEFENSE_POWER;
        this.speed = BASICZOMBIE_SPEED;
    }

    @Override
    public Image getImage()  {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/basicZombie.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
}

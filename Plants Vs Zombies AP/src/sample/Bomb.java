package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Bomb extends Plant {
    public Bomb(int health) {
        super(health);
    }
    public abstract int getBlastRadius();
}

class CherryBomb extends Bomb {
    private static final int CHERRY_BLAST_RADIUS=1;
    private static final int CHERRY_COST=150;
    private static final int CHERRY_RECHARGE_TIME=10;
    private static final int CHERRY_HEALTH=1000000;
    private static Image image;
    private Timeline blastTimeline;
    private HashMap<Integer, ArrayList<Zombie>> zombies;
    public CherryBomb(HashMap<Integer,ArrayList<Zombie>> zombies) {
        super(CHERRY_HEALTH);
        this.zombies=zombies;
    }

    public Timeline getBlastTimeline() {
        return blastTimeline;
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

    public void blast(Pane gamePane,Image blank) {
        blastTimeline=new Timeline(new KeyFrame(Duration.seconds(3),e-> {
            imageView.setDisable(true);
            imageView.setVisible(false);
            imageView.setImage(blank);
            imageView.setOpacity(0);
            for(int row:zombies.keySet()) {
                ArrayList<Zombie> zombieRow=zombies.get(row);
                for(int i=zombieRow.size()-1;i>=0;i--) {
                    ImageView temp=zombieRow.get(i).getImageView();
                    if(shouldZombieDie(temp)) {
                        temp.setVisible(false);
                        temp.setDisable(true);
                        gamePane.getChildren().remove(temp);
                        zombieRow.get(i).remove();
                        zombieRow.remove(i);
                    }
                }
            }
            alive=false;
            health=0;
        }));
        blastTimeline.setCycleCount(1);
        blastTimeline.play();
    }

    @Override
    public int getType() {
        return 2;
    }

    private boolean shouldZombieDie(ImageView zombie) {
        double x_diff=zombie.getLayoutX()-imageView.getLayoutX();
        double y_diff=zombie.getLayoutY()-imageView.getLayoutY();
        return x_diff <= 112 && x_diff >= -85 && y_diff <= 100 && y_diff >= -100;
    }
}
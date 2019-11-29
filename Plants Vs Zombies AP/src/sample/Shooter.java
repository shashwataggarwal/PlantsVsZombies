package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Shooter extends Plant {
    public Shooter(int health) {
        super(health);
    }
    public abstract int getShootingSpeed();
    public abstract void shoot();
}


class Peashooter extends Shooter {
    private static final int PEASHOOTER_COST=100;
    private static final int PEASHOOTER_RECHARGE_TIME=5;
    private static final int PEASHOOTER_HEALTH=100;
    private static final int PEASHOOTER_SHOOTING_SPEED=3;
    private static Image image;
    private Timeline shootTimeline;
    public Peashooter() {
        super(PEASHOOTER_HEALTH);
    }

    public Timeline getShootTimeLine(){
        return shootTimeline;
    }
    @Override
    public Image getImage() {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/peashooter.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public void setShootTimeline(Timeline shootTimeline) {
        this.shootTimeline = shootTimeline;
    }

    public Timeline getShootTimeline() {
        return shootTimeline;
    }

    @Override
    public int getShootingSpeed() {
        return PEASHOOTER_SHOOTING_SPEED;
    }

    @Override
    public void shoot() {

    }

    @Override
    public int getCost() {
        return PEASHOOTER_COST;
    }

    @Override
    public int getRechargeTime() {
        return PEASHOOTER_RECHARGE_TIME;
    }

    public class PeaBullet extends Positionable implements Bullet{
        private static final int PEABULLET_SPEED=5;
        private static final int PEABULLET_DAMAGE=5;
        private ArrayList<Zombie> zombies;
        private Image image;
        @Override
        public int getDamage() {
            return PEABULLET_DAMAGE;
        }

        @Override
        public int getBulletSpeed() {
            return PEABULLET_SPEED;
        }


        public PeaBullet(ArrayList<Zombie> zombies) {
            this.zombies=zombies;
        }

        @Override
        public Image getImage() {
            if(image==null) {
                try {
                    image=new Image(new FileInputStream("./images/pea.png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return image;
        }


        @Override
        public void remove() {
            shootTimeline.stop();
        }

        @Override
        public void startMovement(int dir, int offset, int distance, int milli, boolean remove, Pane gamePane) {
            movementTimeline=new Timeline(new KeyFrame(Duration.millis(milli), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(dir==0) {
                        imageView.setLayoutY(imageView.getLayoutY()+offset);
                    }
                    else if(dir==1) {
                        imageView.setLayoutX(imageView.getLayoutX()-offset);
                    }
                    else {
                        imageView.setLayoutX(imageView.getLayoutX()+offset);
                    }
                    for(int i=zombies.size()-1;i>=0;i--) {
                        if(intersects(imageView,zombies.get(i).getImageView())) {
                            imageView.setDisable(true);
                            imageView.setVisible(false);
                            gamePane.getChildren().remove(imageView);
                            zombies.get(i).reduceHealth(PEABULLET_DAMAGE);
                            if(!zombies.get(i).isAlive()) {
                                zombies.get(i).getImageView().setDisable(true);
                                zombies.get(i).getImageView().setVisible(false);
                                zombies.get(i).remove();
                                gamePane.getChildren().remove(zombies.get(i).getImageView());
                                zombies.remove(i);
                            }
                        }
                    }
                }
            }));
            if(distance>3000) {
                movementTimeline.setCycleCount(Timeline.INDEFINITE);
            }
            else {
                movementTimeline.setCycleCount((int)(distance/offset));
            }
            movementTimeline.play();
            if(remove) {
                movementTimeline.setOnFinished(e-> {
                    imageView.setDisable(true);
                    imageView.setVisible(false);
                    gamePane.getChildren().remove(imageView);
                });
            }
        }
        private boolean intersects(ImageView x,ImageView y) {
            int offset=(int)(y.getLayoutX()-x.getLayoutX());
            offset=(offset>0)?offset:-offset;
            if(offset<5) return true;
            return false;

        }


    }


}


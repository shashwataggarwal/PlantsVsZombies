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

public class ShooterBombSunflower extends Plant {
    private static final int SBS_COST=250;
    private static final int SBS_RECHARGE_TIME=15;
    private static final int SBS_TIME=5;
    private static final int SBS_SHOOTING_SPEED=3;
    private static final int SBS_BLAST_RADIUS=2;
    private static final int SBS_HEALTH=100000;
    private static final int SBL_PRODUCTION_TIME=10;
    private Timeline shootingTimeline;
    private Timeline sunTimeline;
    private static Image image;
    private HashMap<Integer,ArrayList<Zombie>> zombies;
    public ShooterBombSunflower(HashMap<Integer,ArrayList<Zombie>> zombies) {
        super(SBS_HEALTH);
        this.zombies=zombies;
    }

    public static int getSblProductionTime() {
        return SBL_PRODUCTION_TIME;
    }

    public Timeline getShootingTimeline() {
        return shootingTimeline;
    }


    public static int getSbsShootingSpeed() {
        return SBS_SHOOTING_SPEED;
    }

    public void setShootingTimeline(Timeline shootingTimeline) {
        this.shootingTimeline = shootingTimeline;
    }

    public Timeline getSunTimeline() {
        return sunTimeline;
    }

    public void setSunTimeline(Timeline sunTimeline) {
        this.sunTimeline = sunTimeline;
    }

    @Override
    public Image getImage() {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/special.gif"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public int getRechargeTime() {
        return SBS_RECHARGE_TIME;
    }

    @Override
    public int getCost() {
        return SBS_COST;
    }

    public class SpecialBullet extends Positionable implements Bullet {
        private static final int SBL_DAMAGE=5;
        private static final int SBL_SPEED=10;
        private ArrayList<Zombie> zombies;
        private Image image;

        public SpecialBullet(ArrayList<Zombie> zombies) {
            this.zombies=zombies;
        }
        @Override
        public int getBulletSpeed() {
            return SBL_SPEED;
        }

        @Override
        public int getDamage() {
            return SBL_DAMAGE;
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
                            zombies.get(i).reduceHealth(SBL_DAMAGE);
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
        private boolean intersects(ImageView x, ImageView y) {
            int offset=(int)(y.getLayoutX()-x.getLayoutX());
            offset=(offset>0)?offset:-offset;
            if(offset<5) return true;
            return false;

        }
    }

    @Override
    public void remove() {
        shootingTimeline.stop();
        sunTimeline.stop();
    }

    public void blast(Pane gamePane,Image blank) {
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
    }

    public boolean shouldZombieDie(ImageView zombie) {
        double x_diff=zombie.getLayoutX()-imageView.getLayoutX();
        double y_diff=zombie.getLayoutY()-imageView.getLayoutY();
        if(x_diff<=112 && x_diff>=-56 && y_diff<=100 && y_diff>=-100) {
            return true;
        }
        return false;
    }

    @Override
    public int getType() {
        return 4;
    }
}

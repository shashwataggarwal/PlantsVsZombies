package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;

public abstract class Zombie extends Character {
    protected int attackPower;
    protected int defensePower;
    protected int speed;
    protected HashMap<Integer,Plant> plants;
    protected boolean move=true;
    public Zombie(int health,HashMap<Integer,Plant> plants) {
        super(health);
        this.plants=plants;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public void setDefensePower(int defensePower) {
        this.defensePower = defensePower;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public void move(){

    }

    public void reduceHealth(int amount) {
        health-=amount;
        if(health<=0) {
            health=0;
            alive=false;
        }
    }

    public void startMovement(int dir,int offset,int distance,Image blank,LawnMower lawnMower,Level level,Pane gamePane) {
        startMovement(dir,offset,distance,150,false,gamePane,blank,lawnMower,level);
    }

    public void startMovement(int dir, int offset, int distance, int milli, boolean remove, Pane gamePane,Image blank,LawnMower lawnMower,Level level) {
        // dir=0 : top - > down
        // dir=1 : right - > left
        // dir=2 : left - > right
        movementTimeline=new Timeline(new KeyFrame(Duration.millis(milli), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(move) {
                    if (dir == 0) {
                        imageView.setLayoutY(imageView.getLayoutY() + offset);
                    } else if (dir == 1) {
                        imageView.setLayoutX(imageView.getLayoutX() - offset);
                    } else {
                        imageView.setLayoutX(imageView.getLayoutX() + offset);
                    }
                }
                int counter=0;
                for(int i=0;i<8;i++) {
                    if(plants.containsKey(i)) {
                        Plant v=plants.get(i);
                        if (intersects(imageView, v.getImageView())) {
                            if(v instanceof ShooterBombSunflower) {
                                ((ShooterBombSunflower) v).blast(gamePane,blank);
                            }
                            move = false;
                            counter++;
                            v.reduceHealth(getAttackPower());
                            if (!v.isAlive()) {
                                v.getImageView().setImage(blank);
                                v.getImageView().setOpacity(0);
                                v.remove();
                                plants.remove(i);
                                move = true;
                            }
                        }
                    }
                }
                if(counter==0) {
                    move=true;
                }
                if(intersects(lawnMower.getImageView(),imageView)) {
                    if(!lawnMower.isUsed() && !lawnMower.isInUse()) {
                        lawnMower.use(gamePane);
                    }
                    else if(lawnMower.isUsed()){
                        System.out.println("GAME LOST");
                        level.loseGame();
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

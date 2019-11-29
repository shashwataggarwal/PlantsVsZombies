package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.Serializable;

public abstract class Positionable implements Serializable {
    private int x;
    private int y;
    protected ImageView imageView;
    protected Timeline movementTimeline;
    public Positionable(int x,int y)  {
        this.x=x;
        this.y=y;
        this.imageView=null;
        movementTimeline=null;
    }

    public void startMovement(int dir,int offset,int distance) {
        startMovement(dir,offset,distance,150,false,null);
    }

    public void startMovement(int dir,int offset,int distance,int milli,boolean remove,Pane gamePane) {
        // dir=0 : top - > down
        // dir=1 : right - > left
        // dir=2 : left - > right
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

    public Positionable() {
        this.imageView=null;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract Image getImage();

    public void remove() {
        if(movementTimeline!=null) {
            movementTimeline.stop();
        }
    }

    public Timeline getMovementTimeline() {
        return movementTimeline;
    }
}

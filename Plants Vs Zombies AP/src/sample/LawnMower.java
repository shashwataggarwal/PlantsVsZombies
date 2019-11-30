package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

public class LawnMower extends Positionable{
    private final int speed;
    private boolean used;
    private final int row;
    private ArrayList<Zombie> zombies;
    private int initialX;
    private boolean inUse;
    public LawnMower(int row, ArrayList<Zombie> zombies) {
        speed=4;
        used=false;
        this.row=row;
        this.zombies=zombies;
        this.inUse=false;
    }
    public boolean isUsed() {
        return used;
    }

    public boolean isInUse() { return inUse;}

    public void use(Pane gamePane,Level level) {
        inUse=true;
        initialX=(int)imageView.getLayoutX();
        movementTimeline=new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                imageView.setLayoutX(imageView.getLayoutX()+speed);
                for(int i=zombies.size()-1;i>=0;i--) {
                    if(zombies.get(i).getImageView().getLayoutX()<imageView.getLayoutX()) {
                        Zombie curr=zombies.get(i);
                        curr.getImageView().setDisable(true);
                        curr.getImageView().setVisible(false);
                        curr.remove();
                        try {
                            gamePane.getChildren().remove(curr.getImageView());
                            zombies.remove(i);
                        }
                        catch (NullPointerException e) {}
                    }
                }
            }
        }));
        movementTimeline.setCycleCount((int)(720-initialX)/speed);
        movementTimeline.setOnFinished(e-> {
            used=true;
            inUse=false;
            imageView.setLayoutX(initialX);
            imageView.setVisible(false);
        });
        level.addLawnMowerMovement(movementTimeline);
        movementTimeline.play();
    }
    public int getRow() {
        return row;
    }

    @Override
    public Image getImage() {
        return null;
    }
}

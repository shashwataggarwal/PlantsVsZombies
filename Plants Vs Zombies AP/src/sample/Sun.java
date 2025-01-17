package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Sun extends Positionable {
    private boolean pickedUp;
    private static Image image;
    private int finalY;
    public Sun() {
        pickedUp=false;
        finalY=0;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setFinalY(int finalY) {
        this.finalY = finalY;
    }

    public int getFinalY() {
        return finalY;
    }

    public void pickUp() {
        pickedUp=true;
    }

    @Override
    public Image getImage()  {
        if(image==null) {
            try {
                image=new Image(new FileInputStream("./images/sun.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

}

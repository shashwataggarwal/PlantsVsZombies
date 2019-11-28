package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {

    private final int  levelNumber;
    private final int stripCount;
    private int sunCount;
    private ArrayList<Zombie> zombies;
    private ArrayList<Plant> plants;
    private ArrayList<LawnStrip> lawnStrips;
    private ArrayList<String> availablePlants;
    private boolean levelComplete;
    private final int SUN_TIMER = 10;

    public Level(int levelNumber, int stripCount) {
        this.levelNumber = levelNumber;
        this.stripCount = stripCount;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void buyPlant(String type){

    }

    private void initZombies(){

    }

    private void addZombie(String type){

    }

    public void create(){

    }

    public void play(){

    }

    public boolean gameWon(){
        return levelComplete;
    }

    public void pause(){

    }

    public void resume(){

    }

    public void generateSun(){

    }

    public void pickupSun(){

    }

    public int getSunCount() {
        return sunCount;
    }
}

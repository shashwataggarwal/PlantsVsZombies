package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {
    private ArrayList<PlantData> plants;
    private ArrayList<ZombieData> zombies;
    private ArrayList<BulletData> bullets;
    private ArrayList<SunData> suns;
    private ArrayList<LawnMowerData> lawnMowers;
    private ArrayList<Integer> refreshTimes;
    private int levelNumber;
    private int sunCount;
    private int initialTimerValue;
    private int currentTimerValue;

    public LevelData(ArrayList<PlantData> plants, ArrayList<ZombieData> zombies, ArrayList<BulletData> bullets, ArrayList<SunData> suns, ArrayList<LawnMowerData> lawnMowers, ArrayList<Integer> refreshTimes, int levelNumber, int sunCount, int initialTimerValue, int currentTimerValue) {
        this.plants = plants;
        this.zombies = zombies;
        this.bullets = bullets;
        this.suns = suns;
        this.lawnMowers = lawnMowers;
        this.refreshTimes = refreshTimes;
        this.levelNumber = levelNumber;
        this.sunCount = sunCount;
        this.initialTimerValue = initialTimerValue;
        this.currentTimerValue = currentTimerValue;
    }



    public ArrayList<PlantData> getPlants() {
        return plants;
    }

    public void setPlants(ArrayList<PlantData> plants) {
        this.plants = plants;
    }

    public ArrayList<ZombieData> getZombies() {
        return zombies;
    }

    public void setZombies(ArrayList<ZombieData> zombies) {
        this.zombies = zombies;
    }

    public ArrayList<BulletData> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<BulletData> bullets) {
        this.bullets = bullets;
    }

    public ArrayList<SunData> getSuns() {
        return suns;
    }

    public void setSuns(ArrayList<SunData> suns) {
        this.suns = suns;
    }

    public ArrayList<LawnMowerData> getLawnMowers() {
        return lawnMowers;
    }

    public void setLawnMowers(ArrayList<LawnMowerData> lawnMowers) {
        this.lawnMowers = lawnMowers;
    }

    public ArrayList<Integer> getRefreshTimes() {
        return refreshTimes;
    }

    public void setRefreshTimes(ArrayList<Integer> refreshTimes) {
        this.refreshTimes = refreshTimes;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getSunCount() {
        return sunCount;
    }

    public void setSunCount(int sunCount) {
        this.sunCount = sunCount;
    }

    public int getInitialTimerValue() {
        return initialTimerValue;
    }

    public void setInitialTimerValue(int initialTimerValue) {
        this.initialTimerValue = initialTimerValue;
    }

    public int getCurrentTimerValue() {
        return currentTimerValue;
    }

    public void setCurrentTimerValue(int currentTimerValue) {
        this.currentTimerValue = currentTimerValue;
    }
}

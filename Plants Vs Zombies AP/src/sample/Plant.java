package sample;

public abstract class Plant extends Character {
    public Plant(int health) {
        super(health);
    }
    public abstract int getRechargeTime();
    public abstract int getCost();
    public abstract int getType();
    public void reduceHealth(int amount) {
        health-=amount;
        if(health<=0) {
            alive=false;
            health=0;
        }
    }
}

interface Bullet {
    int getDamage();
    int getBulletSpeed();
}
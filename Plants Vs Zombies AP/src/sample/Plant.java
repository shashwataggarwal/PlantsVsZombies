package sample;

public abstract class Plant extends Character {
    public Plant(int health) {
        super(health);
    }
    public abstract int getRechargeTime();
    public abstract int getCost();
}

interface Bullet {
    int getDamage();
    int getBulletSpeed();
}
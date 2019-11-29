package sample;

import javafx.scene.image.Image;

public abstract class Zombie extends Character {
    protected int attackPower;
    protected int defensePower;
    protected int speed;

    public Zombie(int health) {
        super(health);
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

    public void reduceHealth() {

    }
}

package sample;

import java.io.Serializable;

public abstract class Character extends Positionable implements Serializable {
    private int health;
    private boolean alive;
    public Character(int health) {
        this.health=health;
        this.alive=true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return alive;
    }
    public void death() {
        alive=false;
    }
}

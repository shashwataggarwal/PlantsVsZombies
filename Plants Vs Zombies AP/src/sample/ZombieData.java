package sample;

public class ZombieData extends PositionData {

//    0 -> basicZombie 1 -> boostedZombie
    private int type;
    private int health;

    public ZombieData(int x, int y, int type, int health) {
        super(x, y);
        this.health = health;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

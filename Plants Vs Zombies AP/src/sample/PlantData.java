package sample;

public class PlantData extends PositionData {
    private int health;
    private int type;
    public PlantData(int x, int y, int health, int type) {
        super(x, y);
        this.health =  health;
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

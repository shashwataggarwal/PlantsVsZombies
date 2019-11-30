package sample;

public class BulletData extends PositionData {
    private int type;
    public BulletData(int x, int y, int type) {
        super(x, y);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

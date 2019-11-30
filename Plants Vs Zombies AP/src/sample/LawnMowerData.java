package sample;

public class LawnMowerData extends PositionData {
    private boolean used;
    public LawnMowerData(int x, int y, boolean used) {
        super(x, y);
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

package sample;

public class SunData extends PositionData {
    private int finalY;

    public SunData(int x, int y, int finalY) {
        super(x, y);
        this.finalY = finalY;
    }

    public int getFinalY() {
        return finalY;
    }

    public void setFinalY(int finalY) {
        this.finalY = finalY;
    }
}

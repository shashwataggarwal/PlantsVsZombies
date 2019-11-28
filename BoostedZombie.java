package sample;

public class BoostedZombie extends Zombie{
    private static final int BOOSTEDZOMBIE_ATTACK_POWER = 8;
    private static final int BOOSTEDZOMBIE_DEFENSE_POWER = 3;
    private static final int BOOSTEDZOMBIE_SPEED = 2;

    public BoostedZombie() {
        this.attackPower = BOOSTEDZOMBIE_ATTACK_POWER;
        this.defensePower = BOOSTEDZOMBIE_DEFENSE_POWER;
        this.speed = BOOSTEDZOMBIE_SPEED;
    }
}

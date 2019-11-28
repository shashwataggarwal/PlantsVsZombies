package sample;

public class BasicZombie extends Zombie{
    private static final int BASICZOMBIE_ATTACK_POWER = 5;
    private static final int BASICZOMBIE_DEFENSE_POWER = 2;
    private static final int BASICZOMBIE_SPEED = 1;

    public BasicZombie() {
        this.attackPower = BASICZOMBIE_ATTACK_POWER;
        this.defensePower = BASICZOMBIE_DEFENSE_POWER;
        this.speed = BASICZOMBIE_SPEED;
    }
}

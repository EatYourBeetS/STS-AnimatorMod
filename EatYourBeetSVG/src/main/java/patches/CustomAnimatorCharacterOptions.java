package patches;

import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class CustomAnimatorCharacterOptions {
    private int hp;
    private int gold;

    public CustomAnimatorCharacterOptions()
    {
        hp = AnimatorLoadout.BASE_HP;
        gold = AnimatorLoadout.BASE_GOLD;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}

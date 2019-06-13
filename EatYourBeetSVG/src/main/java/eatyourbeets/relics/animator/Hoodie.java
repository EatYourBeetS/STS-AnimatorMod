package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;

public class Hoodie extends AnimatorRelic
{
    public static final String ID = CreateFullID(Hoodie.class.getSimpleName());

    private static final int MAX_HP_BONUS = 3;

    public Hoodie()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + MAX_HP_BONUS + DESCRIPTIONS[1];
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        if (GameActionManager.damageReceivedThisCombat == 0)
        {
            AbstractDungeon.player.increaseMaxHp(MAX_HP_BONUS, true);
            this.flash();
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.floorNum < 24 && super.canSpawn();
    }
}
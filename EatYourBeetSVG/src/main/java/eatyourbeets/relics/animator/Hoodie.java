package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.Utilities;

public class Hoodie extends AnimatorRelic
{
    public static final String ID = CreateFullID(Hoodie.class.getSimpleName());

    private static final int MAX_HP_BONUS = 3;

    public Hoodie()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return Utilities.Format(DESCRIPTIONS[0], MAX_HP_BONUS);
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
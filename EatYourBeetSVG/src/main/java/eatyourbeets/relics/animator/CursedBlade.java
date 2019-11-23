package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;

public class CursedBlade extends AnimatorRelic
{
    private static final int BUFF_AMOUNT = 4;

    public static final String ID = CreateFullID(CursedBlade.class.getSimpleName());

    public CursedBlade()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], BUFF_AMOUNT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActionsHelper.GainForce(BUFF_AMOUNT);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > AbstractDungeon.player.currentBlock)
        {
            GameActionsHelper.MakeCardInHand(new Wound(), 1, false);
        }

        return super.onAttacked(info, damageAmount);
    }
}
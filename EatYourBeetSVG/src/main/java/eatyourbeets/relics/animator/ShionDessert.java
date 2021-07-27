package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShionDessert extends AnimatorRelic
{
    public static final String ID = CreateFullID(ShionDessert.class);
    public static final int POISON_AMOUNT = 2;

    public ShionDessert()
    {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, POISON_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int mostPoison = -1;
        AbstractMonster enemy = null;

        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            int poison = GameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID);
            if (poison > mostPoison)
            {
                mostPoison = poison;
                enemy = m;
            }
        }

        if (enemy == null)
        {
            enemy = GameUtilities.GetRandomEnemy(true);
        }

        if (enemy != null)
        {
            GameActions.Bottom.Add(new RelicAboveCreatureAction(enemy, this));
            GameActions.Bottom.ApplyPoison(player, enemy, POISON_AMOUNT);
            flash();
        }
    }
}
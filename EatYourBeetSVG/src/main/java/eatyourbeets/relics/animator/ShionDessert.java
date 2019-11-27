package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class ShionDessert extends AnimatorRelic
{
    public static final String ID = CreateFullID(ShionDessert.class.getSimpleName());

    private static final int POISON_AMOUNT = 2;

    public ShionDessert()
    {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], POISON_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int mostPoison = -1;
        AbstractMonster enemy = null;

        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            AbstractPower poison = GameUtilities.GetPower(m, PoisonPower.POWER_ID);
            if (poison != null && poison.amount > mostPoison)
            {
                mostPoison = poison.amount;
                enemy = m;
            }
        }

        if (enemy == null)
        {
            enemy = GameUtilities.GetRandomEnemy(true);
        }

        if (enemy != null)
        {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new RelicAboveCreatureAction(enemy, this));
            GameActionsHelper.ApplyPower(p, enemy, new PoisonPower(enemy, p, POISON_AMOUNT), POISON_AMOUNT);
        }
    }
}
package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.powers.PlayerStatistics;

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
        return Utilities.Format(DESCRIPTIONS[0], POISON_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int mostPoison = -1;
        AbstractMonster enemy = null;

        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            AbstractPower poison = PlayerStatistics.GetPower(m, PoisonPower.POWER_ID);
            if (poison != null && poison.amount > mostPoison)
            {
                mostPoison = poison.amount;
                enemy = m;
            }
        }

        if (enemy == null)
        {
            enemy = PlayerStatistics.GetRandomEnemy(true);
        }

        if (enemy != null)
        {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new RelicAboveCreatureAction(p, this));
            GameActionsHelper.ApplyPower(p, enemy, new PoisonPower(enemy, p, POISON_AMOUNT), POISON_AMOUNT);
        }
    }
}
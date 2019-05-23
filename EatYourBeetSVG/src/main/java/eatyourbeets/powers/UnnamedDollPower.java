package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;

public class UnnamedDollPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPower.class.getSimpleName());

    public UnnamedDollPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    public void onDeath()
    {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.ApplyPower(null, p, new StrengthPower(p, amount), amount);

            for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(null, m, new StrengthPower(m, amount), amount);
            }
        }
    }
}
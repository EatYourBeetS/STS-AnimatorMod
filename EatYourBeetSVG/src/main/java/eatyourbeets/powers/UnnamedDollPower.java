package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

    @Override
    public void updateDescription()
    {
        String[] text = powerStrings.DESCRIPTIONS;
        this.description = text[0] + amount + text[1];
    }

    public void onDeath()
    {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
            {
                GameActionsHelper.ApplyPower(null, c, new StrengthPower(c, amount), amount);
            }
        }
    }
}
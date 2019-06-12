package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class UnnamedDollPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPower.class.getSimpleName());

    private static final int STRENGTH = 30;

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
        this.description = text[0] + amount + text[1] + STRENGTH + text[2];
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        if (action.target != owner)
        {
            owner.increaseMaxHp(amount, true);
        }
    }

    @Override
    public void onDeath()
    {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
            {
                GameActionsHelper.ApplyPower(null, c, new StrengthPower(c, STRENGTH), STRENGTH);
            }
        }
    }
}
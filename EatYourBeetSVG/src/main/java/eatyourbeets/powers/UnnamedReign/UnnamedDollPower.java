package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.Hemokinesis2Effect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.animator.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class UnnamedDollPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPower.class.getSimpleName());

    private int hpGain = 3;
    private static final int STRENGTH = 30;

    public UnnamedDollPower(AbstractCreature owner, int amount, int hpGain)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.hpGain = hpGain;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] text = powerStrings.DESCRIPTIONS;
        if (amount > 0)
        {
            this.description = text[0] + amount + text[1] + hpGain + text[2] + text[3] + STRENGTH + text[4];
        }
        else
        {
            this.description = text[3] + STRENGTH + text[4];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        if (action.target != owner && amount > 0)
        {
            owner.increaseMaxHp(hpGain, true);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        amount -= 1;
        updateDescription();
    }

    @Override
    public void onDeath()
    {
        if (!AbstractDungeon.getCurrRoom().isBattleEnding())
        {
            for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
            {
                GameActionsHelper.VFX(new Hemokinesis2Effect(owner.hb.cX, owner.hb.cY, c.hb.cX, c.hb.cY), 0.35f);
                GameActionsHelper.ApplyPower(null, c, new StrengthPower(c, STRENGTH), STRENGTH);
            }
        }
    }
}
package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.JUtils;

public abstract class OrbCore_AbstractPower extends AnimatorPower
{
    private static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);

    protected int value;
    protected int uses;

    public OrbCore_AbstractPower(String id, AbstractCreature owner, int amount)
    {
        super(owner, id);

        this.enabled = false;
        this.uses = amount;
        this.amount = amount;
    }

    @Override
    public void updateDescription()
    {
        if (enabled)
        {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + this.value + powerStrings.DESCRIPTIONS[2];
        }
        else
        {
            this.description = "Needs 1 more Synergy";
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.uses += stackAmount;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        this.enabled = false;
        this.amount = uses;
        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasSynergy())
        {
            if (!enabled)
            {
                enabled = true;
            }
            else if (amount > 0)
            {
                amount -= 1;

                OnSynergy(player, usedCard);

                this.flash();
            }

            updateDescription();
        }
    }

    protected abstract void OnSynergy(AbstractPlayer p, AbstractCard usedCard);
}
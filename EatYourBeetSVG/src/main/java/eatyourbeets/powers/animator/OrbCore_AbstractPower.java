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

    protected int potency;

    public OrbCore_AbstractPower(String id, AbstractCreature owner, int amount, int potency)
    {
        super(owner, id);

        this.potency = potency;
        SetEnabled(false);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = enabled ? FormatDescription(0, amount, potency) : "Needs 1 more Synergy";
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        SetEnabled(false);
        ResetAmount();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        final AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
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
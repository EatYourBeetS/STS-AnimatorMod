package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;

public abstract class VestaElixirEffect
{
    protected final static String NEWLINE = " NL ";
    protected final static AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    protected final int amount;

    protected VestaElixirEffect(int amount)
    {
        this.amount = amount;
    }

    public abstract String GetDescription();
    public abstract void EnqueueAction(EYBCard elixir, AbstractPlayer player);
}
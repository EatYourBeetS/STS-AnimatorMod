package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class VestaElixirEffect_Discard extends VestaElixirEffect
{
    private final VestaElixirEffect linkedEffect;

    public VestaElixirEffect_Discard(VestaElixirEffect linkedEffect)
    {
        super(2);

        this.linkedEffect = linkedEffect;
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.Discard(amount, true) + NEWLINE + linkedEffect.GetDescription();
    }

    @Override
    public void EnqueueAction(EYBCard elixir, AbstractPlayer player)
    {
        if (GameUtilities.GetOtherCardsInHand(elixir).size() >= amount)
        {
            GameActions.Top.DiscardFromHand(elixir.name, amount, false)
            .SetOptions(false, false, false);

            linkedEffect.EnqueueAction(elixir, player);
        }
    }
}
package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class VestaElixirEffect_Exhaust extends VestaElixirEffect
{
    private final VestaElixirEffect linkedEffect;

    public VestaElixirEffect_Exhaust(int index, VestaElixirEffect linkedEffect)
    {
        super(index, 2);

        this.linkedEffect = linkedEffect;
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        if (GameUtilities.GetOtherCardsInHand(elixir).size() >= amount)
        {
            GameActions.Top.ExhaustFromHand(elixir.name, amount, false)
            .SetOptions(false, false, false);

            linkedEffect.EnqueueAction(elixir, player);
        }
    }

    @Override
    public String GetDescription()
    {
        return super.GetDescription() + " NL " + linkedEffect.GetDescription();
    }
}
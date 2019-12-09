package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
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
            GameActionsHelper_Legacy.AddToBottom(new ExhaustAction(player, player, amount, false, false, false));
            linkedEffect.EnqueueAction(elixir, player);
        }
    }

    @Override
    public String GetDescription()
    {
        return super.GetDescription() + " NL " + linkedEffect.GetDescription();
    }
}
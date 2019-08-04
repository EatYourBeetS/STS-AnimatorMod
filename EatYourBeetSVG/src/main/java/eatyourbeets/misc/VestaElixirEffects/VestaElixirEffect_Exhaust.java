package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;

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
        if (elixir.GetOtherCardsInHand().size() >= amount)
        {
            GameActionsHelper.AddToBottom(new ExhaustAction(player, player, amount, false, false, false));
            linkedEffect.EnqueueAction(elixir, player);
        }
    }

    @Override
    public String GetDescription()
    {
        return super.GetDescription() + " NL " + linkedEffect.GetDescription();
    }
}
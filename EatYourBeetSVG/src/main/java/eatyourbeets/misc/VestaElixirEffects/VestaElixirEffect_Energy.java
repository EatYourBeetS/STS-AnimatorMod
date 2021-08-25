package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Energy extends VestaElixirEffect
{
    public VestaElixirEffect_Energy(boolean upgraded)
    {
        super(upgraded ? 3 : 2);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, "[E]", true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainEnergy(amount);
    }
}
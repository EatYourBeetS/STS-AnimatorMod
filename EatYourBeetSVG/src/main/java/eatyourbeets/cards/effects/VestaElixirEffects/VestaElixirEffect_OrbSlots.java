package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_OrbSlots extends VestaElixirEffect
{
    public VestaElixirEffect_OrbSlots(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainOrbSlots(amount, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainOrbSlots(amount);
    }
}
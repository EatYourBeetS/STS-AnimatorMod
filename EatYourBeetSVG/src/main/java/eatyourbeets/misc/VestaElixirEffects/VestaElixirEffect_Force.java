package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Force extends VestaElixirEffect
{
    public VestaElixirEffect_Force()
    {
        super(3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Force, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainForce(amount);
    }
}
package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Metallicize extends VestaElixirEffect
{
    public VestaElixirEffect_Metallicize(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Metallicize, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainMetallicize(amount);
    }
}
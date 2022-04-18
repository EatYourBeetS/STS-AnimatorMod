package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Intellect extends VestaElixirEffect
{
    public VestaElixirEffect_Intellect(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Intellect, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainIntellect(amount);
    }
}
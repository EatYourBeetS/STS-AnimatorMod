package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_TempHP extends VestaElixirEffect
{
    public VestaElixirEffect_TempHP(boolean upgraded)
    {
        super(upgraded ? 9 : 7);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.TempHP, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainTemporaryHP(amount);
    }
}
package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_CardDraw extends VestaElixirEffect
{
    public VestaElixirEffect_CardDraw(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.Draw(amount, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.Draw(amount);
    }
}
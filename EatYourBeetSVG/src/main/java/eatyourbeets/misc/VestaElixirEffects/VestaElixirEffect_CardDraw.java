package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_CardDraw extends VestaElixirEffect
{
    public VestaElixirEffect_CardDraw(int index)
    {
        super(index, 3);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.Draw(amount);
    }
}
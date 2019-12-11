package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Purge extends VestaElixirEffect
{
    public VestaElixirEffect_Purge(int index)
    {
        super(index, 0);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.Purge(elixir);
    }
}
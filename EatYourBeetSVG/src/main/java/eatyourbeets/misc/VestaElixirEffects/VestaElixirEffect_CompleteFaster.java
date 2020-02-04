package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.GR;

public class VestaElixirEffect_CompleteFaster extends VestaElixirEffect
{
    public VestaElixirEffect_CompleteFaster()
    {
        super(0);
    }

    @Override
    public String GetDescription()
    {
        return GR.GetCardStrings(Vesta_Elixir.ID).EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {

    }
}
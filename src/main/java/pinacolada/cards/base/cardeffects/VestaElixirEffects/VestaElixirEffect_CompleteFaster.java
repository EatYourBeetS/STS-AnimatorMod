package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;

public class VestaElixirEffect_CompleteFaster extends VestaElixirEffect
{
    public VestaElixirEffect_CompleteFaster()
    {
        super(0);
    }

    @Override
    public String GetDescription()
    {
        return Vesta_Elixir.DATA.Strings.EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {

    }
}
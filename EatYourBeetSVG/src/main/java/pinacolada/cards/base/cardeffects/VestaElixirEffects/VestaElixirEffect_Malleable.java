package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_Malleable extends VestaElixirEffect
{
    public VestaElixirEffect_Malleable(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Malleable, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainMalleable(amount);
    }
}
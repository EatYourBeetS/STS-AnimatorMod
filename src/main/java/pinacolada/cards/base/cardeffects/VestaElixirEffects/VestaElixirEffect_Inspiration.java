package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_Inspiration extends VestaElixirEffect
{
    public VestaElixirEffect_Inspiration(boolean upgraded)
    {
        super(upgraded ? 3 : 2);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Inspiration, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainInspiration(amount);
    }
}
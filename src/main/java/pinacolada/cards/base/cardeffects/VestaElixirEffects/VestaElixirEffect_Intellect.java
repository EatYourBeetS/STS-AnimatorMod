package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_Intellect extends VestaElixirEffect
{
    public VestaElixirEffect_Intellect(boolean upgraded)
    {
        super(upgraded ? 8 : 6);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Wisdom, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainWisdom(amount);
    }
}
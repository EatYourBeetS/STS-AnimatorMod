package pinacolada.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_Energy extends VestaElixirEffect
{
    public VestaElixirEffect_Energy(boolean upgraded)
    {
        super(upgraded ? 3 : 2);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, "[E]", true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainEnergy(amount);
    }
}
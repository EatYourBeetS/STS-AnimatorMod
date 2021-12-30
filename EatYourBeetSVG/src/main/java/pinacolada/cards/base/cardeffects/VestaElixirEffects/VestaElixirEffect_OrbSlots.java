package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_OrbSlots extends VestaElixirEffect
{
    public VestaElixirEffect_OrbSlots(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainOrbSlots(amount, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainOrbSlots(amount);
    }
}
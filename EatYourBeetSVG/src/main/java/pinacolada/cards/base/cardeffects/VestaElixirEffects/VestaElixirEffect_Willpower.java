package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class VestaElixirEffect_Willpower extends VestaElixirEffect
{
    public VestaElixirEffect_Willpower(boolean upgraded)
    {
        super(upgraded ? 8 : 6);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Endurance, true);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        PCLActions.Bottom.GainEndurance(amount);
    }
}
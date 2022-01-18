package pinacolada.cards.base.cardeffects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class VestaElixirEffect_Exhaust extends VestaElixirEffect
{
    private final VestaElixirEffect linkedEffect;

    public VestaElixirEffect_Exhaust(VestaElixirEffect linkedEffect)
    {
        super(2);

        this.linkedEffect = linkedEffect;
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.Exhaust(amount, true) + NEWLINE + linkedEffect.GetDescription();
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        if (PCLGameUtilities.GetOtherCardsInHand(elixir).size() >= amount)
        {
            PCLActions.Top.ExhaustFromHand(elixir.name, amount, false)
            .SetOptions(false, false, false);

            linkedEffect.EnqueueAction(elixir, player);
        }
    }
}
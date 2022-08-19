package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

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
    public void EnqueueAction(EYBCard elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainEnergy(amount);
    }
}
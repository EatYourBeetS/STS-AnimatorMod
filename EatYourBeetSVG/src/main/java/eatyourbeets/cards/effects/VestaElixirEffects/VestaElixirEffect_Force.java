package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Force extends VestaElixirEffect
{
    public VestaElixirEffect_Force(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Force, true);
    }

    @Override
    public void EnqueueAction(EYBCard elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainForce(amount);
    }
}
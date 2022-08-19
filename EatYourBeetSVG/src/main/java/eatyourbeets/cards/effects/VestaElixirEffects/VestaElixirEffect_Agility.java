package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Agility extends VestaElixirEffect
{
    public VestaElixirEffect_Agility(boolean upgraded)
    {
        super(upgraded ? 4 : 3);
    }

    @Override
    public String GetDescription()
    {
        return ACTIONS.GainAmount(amount, GR.Tooltips.Agility, true);
    }

    @Override
    public void EnqueueAction(EYBCard elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainAgility(amount);
    }
}
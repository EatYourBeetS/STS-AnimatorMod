package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Purge extends VestaElixirEffect
{
    public VestaElixirEffect_Purge(int index)
    {
        super(0);
    }

    @Override
    public String GetDescription()
    {
        return GR.Tooltips.Purge.title + LocalizedStrings.PERIOD;
    }

    @Override
    public void EnqueueAction(EYBCard elixir, AbstractPlayer player)
    {
        GameActions.Bottom.Purge(elixir);
    }
}
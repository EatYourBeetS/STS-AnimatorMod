package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.JavaUtilities;

public abstract class VestaElixirEffect
{
    private final static String[] TEXT = AnimatorResources.GetCardStrings(Vesta_Elixir.ID).EXTENDED_DESCRIPTION;
    private final String rawDescription;

    public int amount;

    protected VestaElixirEffect(int descriptionIndex, int amount)
    {
        this.amount = amount;
        this.rawDescription = TEXT[descriptionIndex];
    }

    public String GetDescription()
    {
        return JavaUtilities.Format(rawDescription, String.valueOf(amount));
    }

    public abstract void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player);
}
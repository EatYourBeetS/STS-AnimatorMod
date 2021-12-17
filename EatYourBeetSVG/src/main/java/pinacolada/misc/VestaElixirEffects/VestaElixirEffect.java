package pinacolada.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;

public abstract class VestaElixirEffect
{
    protected final static String NEWLINE = " NL ";
    protected final static PCLStrings.Actions ACTIONS = GR.PCL.Strings.Actions;
    protected final int amount;

    protected VestaElixirEffect(int amount)
    {
        this.amount = amount;
    }

    public abstract String GetDescription();
    public abstract void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player);
}
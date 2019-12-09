package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class VestaElixirEffect_Metallicize extends VestaElixirEffect
{
    public VestaElixirEffect_Metallicize(int index)
    {
        super(index, 3);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper_Legacy.ApplyPower(player, player, new MetallicizePower(player, amount), amount);
    }
}
package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Focus extends VestaElixirEffect
{
    public VestaElixirEffect_Focus(int index)
    {
        super(index, 1);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new FocusPower(player, amount), amount);
    }
}
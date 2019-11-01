package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Intellect extends VestaElixirEffect
{
    public VestaElixirEffect_Intellect(int index)
    {
        super(index, 3);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new IntellectPower(player, amount), amount);
    }
}
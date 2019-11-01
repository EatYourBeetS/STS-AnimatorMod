package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Force extends VestaElixirEffect
{
    public VestaElixirEffect_Force(int index)
    {
        super(index, 3);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new ForcePower(player, amount), amount);
    }
}
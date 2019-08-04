package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Strength extends VestaElixirEffect
{
    public VestaElixirEffect_Strength(int index)
    {
        super(index, 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new StrengthPower(player, amount), amount);
    }
}
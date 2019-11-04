package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_TempHP extends VestaElixirEffect
{
    public VestaElixirEffect_TempHP(int index)
    {
        super(index, 7);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.GainTemporaryHP(player, amount);
    }
}
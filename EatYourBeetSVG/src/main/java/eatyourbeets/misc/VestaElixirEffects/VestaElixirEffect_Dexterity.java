package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.powers.animator.DexterityTrainingPower;
import eatyourbeets.utilities.GameActionsHelper;

public class VestaElixirEffect_Dexterity extends VestaElixirEffect
{
    public VestaElixirEffect_Dexterity(int index)
    {
        super(index, 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActionsHelper.ApplyPower(player, player, new DexterityPower(player, amount), amount);
    }
}
package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class VestaElixirEffect_Intellect extends VestaElixirEffect
{
    public VestaElixirEffect_Intellect(int index)
    {
        super(index, AbstractDungeon.cardRandomRng.randomBoolean(0.33f) ? 3 : 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainIntellect(amount);
    }
}
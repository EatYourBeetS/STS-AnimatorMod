package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_TempHP extends VestaElixirEffect
{
    public VestaElixirEffect_TempHP(int index)
    {
        super(index, AbstractDungeon.cardRandomRng.random(6, 9));
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainTemporaryHP(amount);
    }
}
package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;

public class VestaElixirEffect_Force extends VestaElixirEffect
{
    public VestaElixirEffect_Force(int index)
    {
        super(index, AbstractDungeon.cardRandomRng.randomBoolean(0.33f) ? 3 : 2);
    }

    @Override
    public void EnqueueAction(Vesta_Elixir elixir, AbstractPlayer player)
    {
        GameActions.Bottom.GainForce(amount);
    }
}
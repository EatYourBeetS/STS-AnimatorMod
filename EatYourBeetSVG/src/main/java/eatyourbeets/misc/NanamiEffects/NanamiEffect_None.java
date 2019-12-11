package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_None extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActions.Bottom.Draw(10);
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(DRAW, 10);
    }
}
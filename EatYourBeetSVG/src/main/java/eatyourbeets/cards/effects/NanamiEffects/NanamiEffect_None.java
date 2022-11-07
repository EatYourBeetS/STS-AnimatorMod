package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class NanamiEffect_None extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(10);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.Draw(10, true);
    }
}
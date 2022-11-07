package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class NanamiEffect_Buff extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(GetForce(nanami));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GetForceTooltip(nanami), true);
    }

    private int GetForce(EYBCard nanami)
    {
        int energy = nanami.energyOnUse;
        if (energy == 0)
        {
            return 1;
        }
        else if (nanami.upgraded)
        {
            return 2 + (energy / 2) + energy;
        }
        else
        {
            return 1 + energy;
        }
    }
}
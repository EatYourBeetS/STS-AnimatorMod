package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Sleep extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPoison(p, m, GetPoison(nanami));
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.Apply(GetPoison(nanami), GR.Tooltips.Poison, true);
    }

    private int GetPoison(Nanami nanami)
    {
        return 3 * (nanami.energyOnUse + (nanami.upgraded ? 2 : 1));
    }
}
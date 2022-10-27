package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Magic extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int orbs = GetOrbs(nanami);

        GameActions.Bottom.ChannelRandomOrb(orbs);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.ChannelRandomOrbs(GetOrbs(nanami), true);
    }

    private int GetOrbs(EYBCard nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}
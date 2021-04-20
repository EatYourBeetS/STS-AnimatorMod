package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Magic extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int orbs = GetOrbs(nanami);

        GameActions.Bottom.ChannelRandomOrbs(orbs);
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.ChannelRandomOrbs(GetOrbs(nanami), true);
    }

    private int GetOrbs(Nanami nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}
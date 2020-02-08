package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameActions;

public class NanamiEffect_Attack extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
    }

    @Override
    public int GetBlock(Nanami nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return "";
    }
}
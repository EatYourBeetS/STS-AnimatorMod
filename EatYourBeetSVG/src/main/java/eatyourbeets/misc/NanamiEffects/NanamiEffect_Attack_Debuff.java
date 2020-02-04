package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Attack_Debuff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int block = GetBlock(nanami);
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.ApplyWeak(p, m, GetWeak(nanami));
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.Apply(GetWeak(nanami), GR.Tooltips.Weak.title, true);
    }

    @Override
    public int GetBlock(Nanami nanami)
    {
        if (nanami.energyOnUse > 0)
        {
            return ModifyBlock ((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
        }
        else
        {
            return 0;
        }
    }

    private int GetWeak(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}
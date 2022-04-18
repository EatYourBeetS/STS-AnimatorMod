package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class NanamiEffect_Attack_Buff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
        GameActions.Bottom.GainForce(GetForce(nanami));
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Force, true);
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

    private int GetForce(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}
package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Unknown extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainEnergy(1);

        int temporaryHP = GetTempHP(nanami);
        if (temporaryHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(temporaryHP);
        }
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.GainAmount(1, GR.Tooltips.Energy,true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo(Nanami nanami)
    {
        return TempHPAttribute.Instance.SetCard(nanami).SetText(String.valueOf(GetTempHP(nanami)), Settings.CREAM_COLOR);
    }

    private int GetTempHP(Nanami nanami)
    {
        return nanami.energyOnUse * (nanami.upgraded ? 6 : 5);
    }
}
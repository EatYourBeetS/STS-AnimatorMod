package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Unknown extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainEnergy(1);

        int temporaryHP = GetTempHP(nanami);
        if (temporaryHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(temporaryHP);
        }
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.GainAmount(1, GR.Tooltips.Energy,true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo(EYBCard nanami)
    {
        return TempHPAttribute.Instance.SetCard(nanami).SetText(String.valueOf(GetTempHP(nanami)), Settings.CREAM_COLOR);
    }

    private int GetTempHP(EYBCard nanami)
    {
        return nanami.energyOnUse * (nanami.upgraded ? 6 : 5);
    }
}
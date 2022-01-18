package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Unknown extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainEnergy(1);

        int temporaryHP = GetTempHP(nanami);
        if (temporaryHP > 0)
        {
            PCLActions.Bottom.GainTemporaryHP(temporaryHP);
        }
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.GainAmount(1, GR.Tooltips.Energy,true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo(PCLCard nanami)
    {
        return TempHPAttribute.Instance.SetCard(nanami).SetText(String.valueOf(GetTempHP(nanami)), Settings.CREAM_COLOR);
    }

    private int GetTempHP(PCLCard nanami)
    {
        return nanami.energyOnUse * (nanami.upgraded ? 6 : 5);
    }
}
package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MikuIzayoi extends PCLCard
{
    public static final PCLCardData DATA = Register(MikuIzayoi.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None, true)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public MikuIzayoi()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetAffinity_Light(1, 0, 1);
        SetEthereal(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetEthereal(true);
                Initialize(0, 0, 5, 4);
            }
            else {
                SetEthereal(false);
                Initialize(0, 0, 2, 4);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.GainEnergyNextTurn(1);
        if (CheckSpecialCondition(true)) {
            PCLActions.Bottom.GainInspiration(1);
        }

        if (PCLGameUtilities.GetCurrentMatchCombo() >= secondaryValue && PCLGameUtilities.IsSameSeries(this,info.PreviousCard) && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.Motivate(1);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (card.type == CardType.POWER)
            {
                return true;
            }
        }
        return false;
    }
}
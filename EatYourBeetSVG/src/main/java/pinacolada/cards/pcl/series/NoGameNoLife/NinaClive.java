package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class NinaClive extends PCLCard
{
    public static final PCLCardData DATA = Register(NinaClive.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public NinaClive()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0,0,1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);

        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.DESCRIPTION, true);
            SetExhaust(false);
        }
        else {
            this.cardText.OverrideDescription(null, true);
            SetExhaust(true);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = upgraded && auxiliaryData.form == 1 ? magicNumber : 1;
        PCLActions.Bottom.SelectFromPile(name, amount, player.hand, player.discardPile)
        .SetOptions(false, true)
        .SetMessage(GR.PCL.Strings.GridSelection.Give(amount, GR.Tooltips.Delayed.title))
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards) {
                PCLActions.Bottom.ModifyTag(c,DELAYED,true);
            }

            if (cards.size() > 0) {
                PCLActions.Bottom.SelectFromPile(name, cards.size(), player.hand)
                        .SetOptions(false, true)
                        .SetMessage(GR.PCL.Strings.GridSelection.Give(cards.size(), GR.Tooltips.Innate.title))
                        .AddCallback(cards2 -> {
                            for (AbstractCard c2 : cards2) {
                                PCLActions.Bottom.ModifyTag(c2, PCL_INNATE,true);
                            }
                        });
            }
        });
    }


}
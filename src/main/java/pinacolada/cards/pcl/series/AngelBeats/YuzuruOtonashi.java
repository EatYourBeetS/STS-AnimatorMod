package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YuzuruOtonashi extends PCLCard
{
    public static final PCLCardData DATA = Register(YuzuruOtonashi.class).SetSkill(1, CardRarity.COMMON, PCLCardTarget.Self).SetSeriesFromClassPackage();

    public YuzuruOtonashi()
    {
        super(DATA);

        Initialize(0, 1, 2, 3);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ExhaustFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLActions.Bottom.GainTemporaryHP(secondaryValue);
                if (c instanceof PCLCard && PCLGameUtilities.IsHindrance(c)) {
                    ((PCLCard) c).affinities.Add(PCLAffinity.Orange, 1);
                }
            }
        });
    }

    @Override
    public void triggerOnPurge() {
        PCLActions.Bottom.GainTemporaryHP(secondaryValue);
    }
}
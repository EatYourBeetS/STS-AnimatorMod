package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YuzuruOtonashi extends PCLCard
{
    public static final PCLCardData DATA = Register(YuzuruOtonashi.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage();

    public YuzuruOtonashi()
    {
        super(DATA);

        Initialize(0, 1, 2, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);

        SetProtagonist(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ExhaustFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.POWER)
                {
                    PCLActions.Bottom.GainInvocation(1, true);
                }
                else if (card.type == CardType.SKILL)
                {
                    PCLActions.Bottom.GainTemporaryHP(secondaryValue);
                }
                else if (PCLGameUtilities.IsHindrance(card))
                {
                    PCLActions.Bottom.AddAffinity(PCLAffinity.Orange, 2);
                }
            }
        });
    }
}
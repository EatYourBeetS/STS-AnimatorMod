package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DolaCouronne extends PCLCard
{
    public static final PCLCardData DATA = Register(DolaCouronne.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public DolaCouronne()
    {
        super(DATA);

        Initialize(0, 8, 2, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            PCLActions.Bottom.GainBlock(secondaryValue);
            PCLActions.Bottom.GainArtifact(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                if (PCLGameUtilities.HasBlueAffinity(c)) {
                    PCLActions.Bottom.GainBlock(secondaryValue);
                }
            }
        });
    }
}
package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.ElricAlphonse_Alt;
import pinacolada.utilities.PCLActions;

public class ElricAlphonse extends PCLCard
{
    public static final PCLCardData DATA = Register(ElricAlphonse.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ElricAlphonse_Alt(), true));

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 2, 6, 1);
        SetUpgrade(0, 1, 4);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(0, 0, 1);

        SetEthereal(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.MakeCardInDiscardPile(new ElricAlphonse_Alt()).SetUpgrade(upgraded, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (info.IsSynergizing) {
            PCLActions.Bottom.GainWisdom(secondaryValue);
        }
    }
}
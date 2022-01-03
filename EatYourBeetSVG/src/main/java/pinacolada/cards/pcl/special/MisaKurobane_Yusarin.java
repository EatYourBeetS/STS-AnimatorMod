package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.colorless.MisaKurobane;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class MisaKurobane_Yusarin extends PCLCard
{
    public static final PCLCardData DATA = Register(MisaKurobane_Yusarin.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(MisaKurobane.DATA.Series);

    public MisaKurobane_Yusarin()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TriggerOrbPassive(p.orbs.size())
                .SetFilter(o -> Fire.ORB_ID.equals(o.ID))
                .SetSequential(true);
        PCLActions.Bottom.Motivate(magicNumber);
    }
}
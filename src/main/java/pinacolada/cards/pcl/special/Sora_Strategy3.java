package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.NoGameNoLife.Sora;
import pinacolada.utilities.PCLActions;

public class Sora_Strategy3 extends PCLCard
{
    public static final PCLCardData DATA = Register(Sora_Strategy3.class)
            .SetImagePath(Sora_Strategy1.DATA.ImagePath)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Sora.DATA.Series);

    public Sora_Strategy3()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0,0,0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Exhaust(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded) {
            PCLActions.Bottom.Draw(1);
        }
        PCLActions.Bottom.Cycle(name,magicNumber);
    }
}
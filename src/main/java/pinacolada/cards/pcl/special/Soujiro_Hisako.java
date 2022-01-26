package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.LogHorizon.Soujiro;
import pinacolada.utilities.PCLActions;

public class Soujiro_Hisako extends PCLCard
{
    public static final PCLCardData DATA = Register(Soujiro_Hisako.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Hisako()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetEthereal(true);
        SetEvokeOrbCount(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.EvokeOrb(1)
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                PCLActions.Bottom.GainWisdom(magicNumber);
            }
        });
    }
}
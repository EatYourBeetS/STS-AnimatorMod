package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.series.LogHorizon.Soujiro;
import pinacolada.powers.replacement.TemporaryDrawReductionPower;
import pinacolada.utilities.PCLActions;

public class Soujiro_Nazuna extends PCLCard
{
    public static final PCLCardData DATA = Register(Soujiro_Nazuna.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Nazuna()
    {
        super(DATA);

        Initialize(0, 0, 6, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1, true));
    }
}
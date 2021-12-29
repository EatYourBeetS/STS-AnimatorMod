package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.colorless.Magilou;
import pinacolada.utilities.PCLActions;

public class Magilou_Bienfu extends PCLCard
{
    public static final PCLCardData DATA = Register(Magilou_Bienfu.class)
            .SetSkill(-2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Magilou.DATA.Series);

    public Magilou_Bienfu()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Blue(1);

        SetLoyal(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        PCLActions.Bottom.GainWisdom(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        PCLActions.Bottom.TriggerOrbPassive(1, true, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return false;
    }
}
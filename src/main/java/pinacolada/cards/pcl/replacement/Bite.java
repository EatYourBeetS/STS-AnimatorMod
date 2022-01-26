package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;

public class Bite extends PCLCard
{
    public static final PCLCardData DATA = Register(Bite.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Normal, PCLCardTarget.Normal)
            .SetColor(CardColor.COLORLESS);

    public Bite()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(3, 0, 0);

        SetAffinity_Dark(1);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BITE);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        if (m != null && m.hasPower(DelayedDamagePower.POWER_ID)) {
            PCLActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}
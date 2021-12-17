package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.MightStance;
import pinacolada.utilities.PCLActions;

public class Alexander extends PCLCard
{
    public static final PCLCardData DATA = Register(Alexander.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Alexander()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.PlayCopy(this, null);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);
        if (MightStance.IsActive())
        {
            PCLActions.Bottom.Draw(1);
        }
    }
}
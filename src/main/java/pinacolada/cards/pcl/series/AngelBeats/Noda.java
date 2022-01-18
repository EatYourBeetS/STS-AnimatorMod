package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.utilities.PCLActions;

public class Noda extends PCLCard
{
    public static final PCLCardData DATA = Register(Noda.class).SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public Noda()
    {
        super(DATA);

        Initialize(12, 0, 2, 3);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAfterlife(true);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.StackAffinityPower(PCLAffinity.Red, magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
        }
    }
}
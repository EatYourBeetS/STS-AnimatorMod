package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PowerHelper;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.utilities.PCLActions;

public class Noda extends PCLCard
{
    public static final PCLCardData DATA = Register(Noda.class).SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public Noda()
    {
        super(DATA);

        Initialize(12, 0, 1, 4);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);
        SetAfterlife(true);

        SetAffinityRequirement(PCLAffinity.Red, 3);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Exhaust(this);
        PCLActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryStrength, magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (info.IsSynergizing || TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
        }
    }
}
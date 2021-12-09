package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.MightStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;

public class RenjiAbarai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RenjiAbarai.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public RenjiAbarai()
    {
        super(DATA);

        Initialize(10, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Red(1, 0, 2);

        SetAffinityRequirement(Affinity.Red, 4);
        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (CheckAffinity(Affinity.Red) && CheckAffinity(Affinity.Green))
        {
            SetAttackType(EYBAttackType.Piercing);
        }
        else
        {
            SetAttackType(EYBAttackType.Normal);
        }

        return super.GetDamageInfo();
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (!VelocityStance.IsActive() || !MightStance.IsActive()){
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
        }
    }
}
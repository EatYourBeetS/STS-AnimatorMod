package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class RenjiAbarai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RenjiAbarai.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public RenjiAbarai()
    {
        super(DATA);

        Initialize(10, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Fire(2, 0, 2);

        SetAffinityRequirement(Affinity.Fire, 2);
        SetAffinityRequirement(Affinity.Air, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (CheckAffinity(Affinity.Fire) && CheckAffinity(Affinity.Air))
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
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);

        if (!AgilityStance.IsActive() || !ForceStance.IsActive()){
            GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
        }
    }
}
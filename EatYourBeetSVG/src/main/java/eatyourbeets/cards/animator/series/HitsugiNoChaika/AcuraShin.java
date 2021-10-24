package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.PoisonAffinityPower;
import eatyourbeets.utilities.GameActions;

public class AcuraShin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraShin.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraShin()
    {
        super(DATA);

        Initialize(5,0,2, 3);
        SetUpgrade(0,0,0,2);

        SetAffinity_Air(1);
        SetAffinity_Poison(2);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Air, 6);
        SetAffinityRequirement(Affinity.Poison, 6);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.StackPower(new PoisonAffinityPower(p, secondaryValue));

        if (CheckAffinity(Affinity.Air) || CheckAffinity(Affinity.Poison))
        {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}
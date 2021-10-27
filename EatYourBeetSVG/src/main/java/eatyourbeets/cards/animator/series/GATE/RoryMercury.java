package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoryMercury extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoryMercury.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public RoryMercury()
    {
        super(DATA);

        Initialize(4, 0, 10, 3);
        SetUpgrade(2, 0, 0);

        SetAffinity_Fire();
        SetAffinity_Earth();
        SetAffinity_Poison();

        SetAffinityRequirement(Affinity.Fire, 8);
        SetAffinityRequirement(Affinity.Earth, 8);
        SetAffinityRequirement(Affinity.Poison, 8);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();

        if (CheckAffinity(Affinity.Fire))
        {
            GameActions.Bottom.GainPanache(secondaryValue);
        }

        if (CheckAffinity(Affinity.Earth))
        {
            GameActions.Bottom.GainPanache(secondaryValue);
        }

        if (CheckAffinity(Affinity.Poison))
        {
            GameActions.Bottom.GainPanache(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<2; i++) {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY)
            .AddCallback(enemy -> {
                if (GameUtilities.GetPowerAmount(SupportDamagePower.POWER_ID) >= magicNumber)
                {
                    GameActions.Top.GainCounterAttack(damage);
                }
            });
        }
    }
}
package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class ReimuHakurei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReimuHakurei.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public ReimuHakurei()
    {
        super(DATA);

        Initialize(4, 0, 1, 2);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);

        SetProtagonist(true);
        SetHarmonic(true);

        SetAffinityRequirement(Affinity.Light, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int amount = TrySpendAffinity(Affinity.Light) ? magicNumber + 1 : magicNumber;

        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.ApplyVulnerable(p, m, amount);
        GameActions.Bottom.ApplyWeak(p, m, amount);
        if (IsStarter())
        {
            GameActions.Bottom.Scry(secondaryValue);
        }
    }
}


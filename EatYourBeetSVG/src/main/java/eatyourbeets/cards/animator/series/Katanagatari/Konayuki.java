package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Konayuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Konayuki.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 0, 3, 12);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 1, 0);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1).IgnoreArtifact(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(magicNumber);
        CombatStats.Affinities.GetPower(Affinity.Red).SetMaximumAmount(secondaryValue);

        if (TryUseAffinity(Affinity.Red))
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(), 1).IgnoreArtifact(true);
        }
    }
}
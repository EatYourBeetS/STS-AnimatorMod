package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class ShunyaKengun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShunyaKengun.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public ShunyaKengun()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 1, 0);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Green, 2);

        SetCardPreview(c -> c.type == CardType.ATTACK && GameUtilities.HasGreenAffinity(c));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.LockOn, 1).IgnoreArtifact(true);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.9f, 1f);
        GameActions.Bottom.Draw(1)
        .SetFilter(c -> c.type == CardType.ATTACK && GameUtilities.HasGreenAffinity(c), false);
    }
}
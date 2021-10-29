package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MariKurokawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MariKurokawa.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public MariKurokawa()
    {
        super(DATA);

        Initialize(5, 0, 2, 3);
        SetUpgrade(3, 0, 0);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 1, 1);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Green, 2);

        SetDrawPileCardPreview(c -> c.type == CardType.ATTACK && GameUtilities.HasGreenAffinity(c));
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.9f, 1f));
        GameActions.Bottom.Draw(1)
        .SetFilter(c -> c.type == CardType.ATTACK && GameUtilities.HasGreenAffinity(c), false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameUtilities.Retain(c);
            }
        });
    }
}
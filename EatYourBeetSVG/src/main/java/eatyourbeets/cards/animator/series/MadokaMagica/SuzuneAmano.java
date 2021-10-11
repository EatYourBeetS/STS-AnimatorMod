package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class SuzuneAmano extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(7, 0, 3, 6);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(Affinity.Water, 3);
    }

    @Override
    protected float GetInitialDamage()
    {
        float damage = super.GetInitialDamage();
        if (IsStarter())
        {
            damage += (JUtils.Count(player.orbs, o -> Fire.ORB_ID.equals(o.ID)) * secondaryValue);
        }

        return damage;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);

        if (IsStarter())
        {
            GameActions.Bottom.Draw(JUtils.Count(player.orbs, o -> Fire.ORB_ID.equals(o.ID)));
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, !upgraded)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(m, (enemy, cards) ->
        {
            if (cards != null && cards.size() > 0)
            {
                GameActions.Bottom.ApplyBurning(CheckAffinity(Affinity.Water) && GameUtilities.IsHindrance(cards.get(0)) ? TargetHelper.Enemies() : TargetHelper.Normal(enemy), magicNumber);
            }
        });
    }
}
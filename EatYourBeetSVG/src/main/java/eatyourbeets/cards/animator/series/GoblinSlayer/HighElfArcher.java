package eatyourbeets.cards.animator.series.GoblinSlayer;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class HighElfArcher extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class)
            .SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(1, 0, 1);

        SetAffinity_Green(2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (CombatStats.Affinities.GetPowerAmount(AffinityType.Green) <= magicNumber)
        {
            GameActions.Bottom.GainAgility(1);
        }

        if (isSynergizing)
        {
            GameActions.Bottom.ModifyAllInstances(uuid)
            .AddCallback(c ->
            {
                if (!c.hasTag(HASTE))
                {
                    c.tags.add(HASTE);
                }
            });
        }
    }
}
package eatyourbeets.cards.animator.series.Katanagatari;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Konayuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Konayuki.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 5, 3, 50);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Red(2);

        SetAffinityRequirement(AffinityType.Red, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber);

        if (CheckAffinity(AffinityType.Red) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.DealDamageToRandomEnemy(secondaryValue, damageTypeForTurn, AttackEffects.NONE)
            .SetDamageEffect(c -> GameEffects.List.Add(new WeightyImpactEffect(c.hb.cX, c.hb.cY)).duration)
            .SetOptions(false, false, false)
            .SetPiercing(true, false);
        }
    }
}
package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoyMustang extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoyMustang.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    public static final int BURNING_ATTACK_BONUS = 15;

    public RoyMustang()
    {
        super(DATA);

        Initialize(6, 0, 2, BURNING_ATTACK_BONUS);
        SetUpgrade(1, 0, 1);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Light(1);

        SetEvokeOrbCount(1);

        SetAffinityRequirement(Affinity.Red, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.FIRE);
        GameActions.Bottom.ChannelOrbs(Fire::new, Math.min(p.orbs.size(), GameUtilities.GetEnemies(true).size()));

        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(BURNING_ATTACK_BONUS));
        }
    }
}
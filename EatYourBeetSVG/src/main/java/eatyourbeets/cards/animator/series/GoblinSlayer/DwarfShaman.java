package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class DwarfShaman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DwarfShaman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public DwarfShaman()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1);

        SetAffinityRequirement(Affinity.General, 3);
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).SetVFX(true, false)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.4f)).duration).SetRealtime(true);
        GameActions.Bottom.ChannelOrb(new Earth());

        if (TrySpendAffinity(Affinity.General))
        {
            GameActions.Bottom.UpgradeFromPile(p.drawPile, 1, false);
        }
    }
}
package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.*;

public class HeavyWarrior extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HeavyWarrior.class)
            .SetAttack(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public HeavyWarrior()
    {
        super(DATA);

        Initialize(30, 0);

        SetAffinity_Red(2, 0, 8);
        SetAffinity_Green(1, 0, 0);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetPlayable(CheckSpecialCondition(false));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb).SetColor(Color.LIGHT_GRAY));
            GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY)
            .SetVFXColor(Color.DARK_GRAY);

            if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS)
            {
                GameActions.Bottom.Motivate(1);
            }
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        boolean canPlayWithoutAffinity = JUtils.Any(player.hand.group, c -> c.uuid != uuid && GameUtilities.IsHighCost(c));
        return canPlayWithoutAffinity || super.CheckSpecialCondition(tryUse);
    }
}
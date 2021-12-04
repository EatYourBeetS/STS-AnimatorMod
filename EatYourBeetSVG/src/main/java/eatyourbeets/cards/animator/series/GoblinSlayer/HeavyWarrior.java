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
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class HeavyWarrior extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HeavyWarrior.class)
            .SetAttack(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public HeavyWarrior()
    {
        super(DATA);

        Initialize(28, 0, 2, 4);

        SetAffinity_Red(2, 0, 6);
        SetAffinity_Orange(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return super.ModifyBlock(enemy, amount + GetXValue());
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(!JUtils.Any(player.hand.group, c -> c.uuid != uuid && c.costForTurn >= 2));
    }

    @Override
    public int GetXValue() {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Red, true) * 2;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(VFX.VerticalImpact(m.hb).SetColor(Color.LIGHT_GRAY));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY).forEach(d -> d
        .SetVFXColor(Color.DARK_GRAY));

        if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS)
        {
            GameActions.Bottom.Motivate(1);
        }

        GameActions.Bottom.GainBlock(GetXValue());
        TrySpendAffinity(Affinity.Red,  CombatStats.Affinities.GetAffinityLevel(Affinity.Red, true));
    }
}
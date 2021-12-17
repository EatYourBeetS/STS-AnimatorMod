package pinacolada.cards.pcl.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class HeavyWarrior extends PCLCard
{
    public static final PCLCardData DATA = Register(HeavyWarrior.class)
            .SetAttack(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public HeavyWarrior()
    {
        super(DATA);

        Initialize(28, 0, 2, 4);

        SetAffinity_Red(1, 0, 6);
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

        SetUnplayable(!PCLJUtils.Any(player.hand.group, c -> c.uuid != uuid && c.costForTurn >= 2));
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Red, true) * 2;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(VFX.VerticalImpact(m.hb).SetColor(Color.LIGHT_GRAY));
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY).forEach(d -> d
        .SetVFXColor(Color.DARK_GRAY));

        if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS)
        {
            PCLActions.Bottom.Motivate(1);
        }

        PCLActions.Bottom.GainBlock(GetXValue());
        TrySpendAffinity(PCLAffinity.Red,  PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Red, true));
    }
}
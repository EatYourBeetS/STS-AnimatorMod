package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.orbs.EarthOrbPassiveAction;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Earth;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class HinanawiTenshi extends PCLCard
{
    public static final PCLCardData DATA = Register(HinanawiTenshi.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Ranged).SetSeriesFromClassPackage(true);

    public HinanawiTenshi()
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetHitCount(2);


        SetAffinityRequirement(PCLAffinity.Blue, 3);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Blue,true));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.4f)).duration).SetRealtime(true));
        int amount = PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Blue,true) / 2;
        if (TrySpendAffinity(PCLAffinity.Blue, amount)) {
            PCLActions.Bottom.AddAffinity(PCLAffinity.Orange, amount);
        }

        Earth firstEarth = PCLJUtils.SafeCast(PCLGameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (firstEarth != null && info.TryActivateSemiLimited()) {
            Earth newEarth = new Earth();
            int projectileCount = firstEarth.projectilesCount;
            PCLActions.Bottom.ChannelOrb(newEarth).AddCallback(() -> {
                firstEarth.projectilesCount = projectileCount / 2;
                newEarth.projectilesCount = projectileCount / 2;
                PCLActions.Top.Add(new EarthOrbPassiveAction(firstEarth, 0));
                PCLActions.Top.Add(new EarthOrbPassiveAction(newEarth, 0));
            });

        }
    }
}


package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class HinanawiTenshi extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(HinanawiTenshi.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public HinanawiTenshi()
    {
        super(DATA);

        Initialize(2, 0, 1, 2);
        SetUpgrade(1, 0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetHitCount(2);


        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.4f)).duration).SetRealtime(true));
        int amount = CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true) / 2;
        if (TrySpendAffinity(Affinity.Blue, amount)) {
            GameActions.Bottom.AddAffinity(Affinity.Orange, amount);
        }

        Earth firstEarth = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (firstEarth != null && info.TryActivateSemiLimited()) {
            Earth newEarth = new Earth();
            int projectileCount = firstEarth.projectilesCount;
            GameActions.Bottom.ChannelOrb(newEarth).AddCallback(() -> {
                firstEarth.projectilesCount = projectileCount / 2;
                newEarth.projectilesCount = projectileCount / 2;
                GameActions.Top.Add(new EarthOrbPassiveAction(firstEarth, 0));
                GameActions.Top.Add(new EarthOrbPassiveAction(newEarth, 0));
            });

        }
    }
}


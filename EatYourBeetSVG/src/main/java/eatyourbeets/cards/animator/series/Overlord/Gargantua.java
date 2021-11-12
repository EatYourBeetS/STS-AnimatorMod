package eatyourbeets.cards.animator.series.Overlord;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.EarthOrbPassiveAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Gargantua extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gargantua.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Brutal)
            .SetSeriesFromClassPackage();

    public Gargantua()
    {
        super(DATA);

        Initialize(5, 9, 1);
        SetUpgrade(2, 2, 1);

        SetAffinity_Red(0,0,2);
        SetAffinity_Orange(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.5f)).duration).SetRealtime(true));
        GameActions.Bottom.GainBlock(block);
        final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (next != null) {
            GameActions.Bottom.StackPower(new GargantuaPower(p, magicNumber));
        }
        else {
            GameActions.Bottom.ChannelOrb(new Earth());
        }

    }

    public static class GargantuaPower extends AnimatorPower
    {
        public GargantuaPower(AbstractPlayer owner, int amount)
        {
            super(owner, Gargantua.DATA);

            this.priority = -99;

            Initialize(amount);
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
        {
            if (damageAmount > 0)
            {
                final Earth next = JUtils.SafeCast(GameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
                if (next != null) {
                    final int temp = damageAmount / Earth.PROJECTILE_DAMAGE;

                    damageAmount = Math.max(0, damageAmount - next.projectilesCount * Earth.PROJECTILE_DAMAGE);
                    GameActions.Top.Add(new EarthOrbPassiveAction(next, -temp));
                    if (next.projectilesCount <= 0)
                    {
                        next.projectilesCount = 0;
                        GameActions.Top.Add(new EvokeSpecificOrbAction(next));
                    }

                    if (info.owner != null && info.owner.isPlayer != owner.isPlayer && damageAmount == 0)
                    {
                        GameActions.Bottom.GainPlatedArmor(1);
                        flashWithoutSound();
                    }
                }
            }

            return super.onAttackedToChangeDamage(info, damageAmount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            RemovePower();
        }
    }
}
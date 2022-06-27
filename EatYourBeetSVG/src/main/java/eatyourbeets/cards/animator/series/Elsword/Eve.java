package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.General), true));

    public static final int POWER_COST = 4;
    public static final int POWER_DAMAGE = 9;

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, POWER_COST, POWER_DAMAGE);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EvePower(p, 1, upgraded));
    }

    public static class EvePower extends AnimatorClickablePower
    {
        private int upgradedAmount = 0;

        public EvePower(AbstractCreature owner, int amount, boolean upgraded)
        {
            super(owner, Eve.DATA, PowerTriggerConditionType.Special, POWER_COST,
                    cost -> CombatStats.Affinities.GetUsableAffinity(Affinity.Star) >= cost,
                    cost -> CombatStats.Affinities.TryUseAffinity(Affinity.Star, cost));

            triggerCondition.SetUses(-1, false, false);

            if (upgraded)
            {
                upgradedAmount += 1;
            }

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, POWER_DAMAGE, amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            CombatStats.Affinities.AddAffinitySealUses(amount);

            final int unupgradedAmount = amount - upgradedAmount;
            if (unupgradedAmount > 0)
            {
                GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.General, false)).Repeat(unupgradedAmount);
            }
            if (upgradedAmount > 0)
            {
                GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.General, true)).Repeat(upgradedAmount);
            }

            flashWithoutSound();
        }

        @Override
        protected void OnSamePowerApplied(AbstractPower power)
        {
            super.OnSamePowerApplied(power);

            final EvePower other = (EvePower) power;
            upgradedAmount += other.upgradedAmount;
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameEffects.Queue.BorderFlash(Color.SKY);
            GameActions.Bottom.DealDamageToRandomEnemy(POWER_DAMAGE, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetOptions(true, false)
            .SetDamageEffect(enemy ->
            {
                SFX.Play(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.9f, 1.1f);
                GameEffects.List.Add(VFX.SmallLaser(owner.hb, enemy.hb, Color.CYAN));
                return 0f;
            });

            this.flash();
        }
    }
}
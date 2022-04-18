package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrbCore_Lightning extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Lightning.class, GR.Tooltips.Lightning)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 2;
    public static final int DAMAGE_AMOUNT = 13;

    public OrbCore_Lightning()
    {
        super(DATA, Lightning::new, 2);

        Initialize(0, 0, DAMAGE_AMOUNT, POWER_ACTIVATION_COST);

        SetAffinity_Light(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_LightningPower(player, 1));
    }

    public static class OrbCore_LightningPower extends AnimatorClickablePower
    {
        public OrbCore_LightningPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Lightning.DATA, PowerTriggerConditionType.Energy, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, DAMAGE_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.ORB_LIGHTNING_CHANNEL);

            for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.LoseBlock(m1, m1.currentBlock);
            }

            final int totalLightning = GameUtilities.GetOrbCount(Lightning.ORB_ID);
            for (int i = 0; i < totalLightning; i++)
            {
                final int[] damage = DamageInfo.createDamageMatrix(DAMAGE_AMOUNT);
                GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.LIGHTNING);
            }

            ReducePower(GameActions.Last, 1);
            SetEnabled(false);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            SetEnabled(true);
        }
    }
}
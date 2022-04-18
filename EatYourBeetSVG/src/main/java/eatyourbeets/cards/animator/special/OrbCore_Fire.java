package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Fire extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Fire.class, GR.Tooltips.Fire)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 1;
    public static final int BURNING_DAMAGE_BONUS = 15;

    public OrbCore_Fire()
    {
        super(DATA, Fire::new, 2);

        Initialize(0, 0, BURNING_DAMAGE_BONUS, POWER_ACTIVATION_COST);

        SetAffinity_Red(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_FirePower(player, 1));
    }

    public static class OrbCore_FirePower extends AnimatorClickablePower
    {
        public OrbCore_FirePower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Fire.DATA, PowerTriggerConditionType.Energy, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, BURNING_DAMAGE_BONUS);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.ATTACK_FIRE);
            GameActions.Bottom.Callback(() -> BurningPower.AddPlayerAttackBonus(BURNING_DAMAGE_BONUS));

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
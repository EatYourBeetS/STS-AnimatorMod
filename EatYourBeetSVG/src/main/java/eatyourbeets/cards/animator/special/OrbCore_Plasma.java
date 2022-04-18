package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, GR.Tooltips.Plasma)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 4;
    public static final int DRAW_AND_HAND_SIZE_BONUS = 1;

    public OrbCore_Plasma()
    {
        super(DATA, Plasma::new, 1);

        Initialize(0, 0, DRAW_AND_HAND_SIZE_BONUS, POWER_ACTIVATION_COST);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Light(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_PlasmaPower(player, 1));
    }

    public static class OrbCore_PlasmaPower extends AnimatorClickablePower
    {
        public OrbCore_PlasmaPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Plasma.DATA, PowerTriggerConditionType.Energy, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);
            this.triggerCondition.requiredAmount += CombatStats.GetCombatData(ID, 0);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, DRAW_AND_HAND_SIZE_BONUS);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.ORB_PLASMA_CHANNEL);
            GameUtilities.ModifyCardDrawPerTurn(1, 1);
            GameUtilities.ModifyEnergyGainPerTurn(1, 1);
            CombatStats.SetCombatData(ID, CombatStats.GetCombatData(ID, 0) + 1);
            this.triggerCondition.requiredAmount += 1;

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
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_Frost extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Frost.class, GR.Tooltips.Frost)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 2;
    public static final int FREEZING_AMOUNT = 2;

    public OrbCore_Frost()
    {
        super(DATA, Frost::new, 2);

        Initialize(0, 0, FREEZING_AMOUNT, POWER_ACTIVATION_COST);

        SetAffinity_Blue(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_FrostPower(player, 1));
    }

    public static class OrbCore_FrostPower extends AnimatorClickablePower
    {
        public OrbCore_FrostPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Frost.DATA, PowerTriggerConditionType.Energy, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, FREEZING_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.ORB_FROST_CHANNEL);
            GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(owner), FREEZING_AMOUNT);

            ReducePower(GameActions.Last, 1);
            SetEnabled(false);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            SetEnabled(true);
        }

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (clickable && enabled && hb.hovered)
            {
                for (EnemyIntent intent : GameUtilities.GetIntents())
                {
                    intent.AddFreezing();
                }
            }
        }
    }
}
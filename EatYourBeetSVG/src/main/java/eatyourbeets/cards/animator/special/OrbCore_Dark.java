package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_Dark extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Dark.class, GR.Tooltips.Dark)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 3;
    public static final int WEAK_TIMES = 2;

    public OrbCore_Dark()
    {
        super(DATA, Dark::new, 2);

        Initialize(0, 0, WEAK_TIMES, POWER_ACTIVATION_COST);

        SetAffinity_Dark(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_DarkPower(player, 1));
    }

    public static class OrbCore_DarkPower extends AnimatorClickablePower
    {
        public OrbCore_DarkPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Dark.DATA, PowerTriggerConditionType.LoseMixedHP, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, WEAK_TIMES);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.ORB_DARK_CHANNEL);
            for (int i = 0; i < WEAK_TIMES; i++)
            {
                GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(owner), 1);
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

        @Override
        public void update(int slot)
        {
            super.update(slot);

            if (clickable && enabled && hb.hovered)
            {
                for (EnemyIntent intent : GameUtilities.GetIntents())
                {
                    intent.AddWeak();
                }
            }
        }
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Aether extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Aether.class, GR.Tooltips.Aether)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 2;
    public static final int CARD_DRAW = 4;

    public OrbCore_Aether()
    {
        super(DATA, Aether::new, 1);

        Initialize(0, 0, CARD_DRAW, POWER_ACTIVATION_COST);

        SetAffinity_Green(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_AetherPower(player, 1));
    }

    public static class OrbCore_AetherPower extends AnimatorClickablePower
    {
        public OrbCore_AetherPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Aether.DATA, PowerTriggerConditionType.DiscardRandom, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, CARD_DRAW);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            SFX.Play(SFX.POWER_FLIGHT, 0.5f);
            GameActions.Bottom.Draw(CARD_DRAW);

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
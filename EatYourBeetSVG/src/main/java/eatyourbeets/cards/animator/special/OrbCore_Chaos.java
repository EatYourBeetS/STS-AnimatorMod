package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.WeightedList;

public class OrbCore_Chaos extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Chaos.class, GR.Tooltips.Chaos)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);
    public static final int POWER_ACTIVATION_COST = 2;
    public static final int RANDOM_ATTACKS_AMOUNT = 3;

    public OrbCore_Chaos()
    {
        super(DATA, Chaos::new, 1);

        Initialize(0, 0, RANDOM_ATTACKS_AMOUNT, POWER_ACTIVATION_COST);

        SetAffinity_Star(2);
    }

    public void ApplyPower()
    {
        GameActions.Bottom.StackPower(new OrbCore_ChaosPower(player, 1));
    }

    public static class OrbCore_ChaosPower extends AnimatorClickablePower
    {
        public OrbCore_ChaosPower(AbstractCreature owner, int amount)
        {
            super(owner, OrbCore_Chaos.DATA, PowerTriggerConditionType.ExhaustRandom, POWER_ACTIVATION_COST);

            this.triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, RANDOM_ATTACKS_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            Chaos.PlayChannelSFX(0.75f);
            final WeightedList<AbstractCard> cards = GameUtilities.GetCardsInCombatWeighted(GenericCondition.FromT1(c -> c.type == CardType.ATTACK));
            for (int i = 0; i < RANDOM_ATTACKS_AMOUNT; i++)
            {
                GameActions.Bottom.MakeCardInHand(cards.Retrieve(rng, true))
                .SetUpgrade(true, true)
                .AddCallback(c -> GameActions.Top.Motivate(c, 1));
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
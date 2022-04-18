package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.UnnamedClickablePower;
import eatyourbeets.utilities.GameActions;

public class LingeringSpirit extends UnnamedCard
{
    public static final int POWER_COST = 2;
    public static final int HEAL_AMOUNT = 4;
    public static final EYBCardData DATA = Register(LingeringSpirit.class)
            .SetMaxCopies(1)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public LingeringSpirit()
    {
        super(DATA);

        Initialize(0, 9, POWER_COST, HEAL_AMOUNT);
        SetUpgrade(0, 4);

        SetSummon(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new SummoningWardPower(p, 1));
    }

    public static class SummoningWardPower extends UnnamedClickablePower
    {
        public SummoningWardPower(AbstractCreature owner, int amount)
        {
            super(owner, LingeringSpirit.DATA, PowerTriggerConditionType.Energy, POWER_COST);

            triggerCondition.SetUses(amount, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_COST, HEAL_AMOUNT);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SummonDoll(1);
            GameActions.Bottom.HealDolls(HEAL_AMOUNT);

            ReducePower(1);
        }
    }
}
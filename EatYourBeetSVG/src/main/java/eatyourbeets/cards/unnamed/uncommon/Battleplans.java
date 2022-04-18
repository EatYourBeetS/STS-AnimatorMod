package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.UnnamedClickablePower;
import eatyourbeets.utilities.GameActions;

public class Battleplans extends UnnamedCard
{
    public static final int POWER_COST = 2;
    public static final EYBCardData DATA = Register(Battleplans.class)
            .SetPower(1, CardRarity.UNCOMMON);

    public Battleplans()
    {
        super(DATA);

        Initialize(0, 0, POWER_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new BattleplansPower(p, 1));
    }

    public static class BattleplansPower extends UnnamedClickablePower
    {
        public BattleplansPower(AbstractCreature owner, int amount)
        {
            super(owner, Battleplans.DATA, PowerTriggerConditionType.DiscardRandom, POWER_COST);

            triggerCondition.SetUses(amount, true, true).SetCondition(__ -> CombatStats.Dolls.Any());

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.SelectDoll(name)
            .AutoSelectSingleTarget(true)
            .AddCallback(doll ->
            {
                if (doll != null)
                {
                    GameActions.Bottom.ActivateDoll(doll, 1);
                }
            });
        }
    }
}
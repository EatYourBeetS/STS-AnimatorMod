package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 2;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.StackPower(new ArpeggioPower(p, 1));
    }

    public static class ArpeggioPower extends AnimatorClickablePower
    {
        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Energy, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.Affinities.AddMaxActivationsPerTurn(Affinity.Blue, amount);
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);

            CombatStats.Affinities.AddMaxActivationsPerTurn(Affinity.Blue, stackAmount);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.Affinities.AddMaxActivationsPerTurn(Affinity.Blue, -amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.ChannelOrbs(Earth::new, Math.min(player.orbs.size(), amount));
        }
    }
}
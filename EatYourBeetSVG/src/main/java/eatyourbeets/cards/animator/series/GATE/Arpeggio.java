package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
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
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(Affinity.Blue, 2);
        SetAffinityRequirement(Affinity.Red, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.StackPower(new ArpeggioPower(p, 1));

        if (CheckAffinity(Affinity.Blue) && CheckAffinity(Affinity.Red)) {
            GameActions.Bottom.ChannelOrb(new Earth());
        }
    }

    public static class ArpeggioPower extends AnimatorPower
    {
        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA);

            Initialize(amount);
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
    }
}
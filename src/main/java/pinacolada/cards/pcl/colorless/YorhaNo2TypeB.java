package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnGainAffinitySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YorhaNo2TypeB extends PCLCard
{
    public static final PCLCardData DATA = Register(YorhaNo2TypeB.class)
            .SetPower(2, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Nier);

    public YorhaNo2TypeB()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetCostUpgrade(-1);

        SetAffinity_Silver(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainArtifact(magicNumber);
        PCLActions.Bottom.StackPower(new YohraNo2TypeBPower(p, this.magicNumber));
    }

    public static class YohraNo2TypeBPower extends PCLPower implements OnShuffleSubscriber, OnGainAffinitySubscriber
    {

        public YohraNo2TypeBPower(AbstractPlayer owner, int amount)
        {
            super(owner, YorhaNo2TypeB.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            PCLCombatStats.onShuffle.Subscribe(this);
            PCLCombatStats.onGainAffinity.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLCombatStats.onShuffle.Unsubscribe(this);
            PCLCombatStats.onGainAffinity.Unsubscribe(this);
        }

        @Override
        public void OnShuffle(boolean triggerRelics) {
            if (!owner.powers.contains(this))
            {
                PCLCombatStats.onShuffle.Unsubscribe(this);
                PCLCombatStats.onGainAffinity.Unsubscribe(this);
                return;
            }

            PCLActions.Bottom.GainArtifact(amount);
            flash();
        }

        @Override
        public int OnGainAffinity(PCLAffinity affinity, int amount, boolean isActuallyGaining) {
            if (affinity == PCLAffinity.Silver) {
                for (AbstractCreature creature : PCLGameUtilities.GetAllCharacters(true)) {
                    PCLActions.Bottom.StackPower(TargetHelper.Normal(creature), PCLPowerHelper.Strength, rng.random(-3, 3)).IgnoreArtifact(true);
                }

                flash();
            }
            return amount;
        }
    }
}
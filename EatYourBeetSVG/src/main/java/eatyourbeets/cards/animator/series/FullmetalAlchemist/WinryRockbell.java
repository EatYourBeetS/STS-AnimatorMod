package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class WinryRockbell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, WinryRockbellPower.BLOCK_AMOUNT);
        SetUpgrade(0, 2, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new WinryRockbellPower(p, 1));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (isSynergizing)
        {
            GameActions.Bottom.UpgradeFromHand(name, 1, false);
        }
    }

    public static class WinryRockbellPower extends AnimatorPower implements OnAfterCardDiscardedSubscriber
    {
        public static final int BLOCK_AMOUNT = 4;

        private int baseAmount;

        public WinryRockbellPower(AbstractCreature owner, int amount)
        {
            super(owner, WinryRockbell.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, BLOCK_AMOUNT);
            SetEnabled(amount > 0);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardDiscarded.Subscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void OnAfterCardDiscarded()
        {
            if (owner == null || !owner.powers.contains(this))
            {
                CombatStats.onAfterCardDiscarded.Unsubscribe(this);
                return;
            }

            if (enabled)
            {
                GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
                this.amount -= 1;
                this.flashWithoutSound();
            }

            updateDescription();
        }
    }
}
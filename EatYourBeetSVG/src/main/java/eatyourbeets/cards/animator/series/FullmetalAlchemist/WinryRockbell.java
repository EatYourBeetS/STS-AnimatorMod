package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class WinryRockbell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(WinryRockbell.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int UPGRADE_CARDS_AMOUNT = 2;
    public static final int BLOCK_AMOUNT = 4;


    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, BLOCK_AMOUNT, UPGRADE_CARDS_AMOUNT);
        SetUpgrade(0, 2);

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

    public static class WinryRockbellPower extends AnimatorClickablePower implements OnAfterCardDiscardedSubscriber
    {
        public WinryRockbellPower(AbstractCreature owner, int amount)
        {
            super(owner, WinryRockbell.DATA, PowerTriggerConditionType.Energy, 1);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, UPGRADE_CARDS_AMOUNT, amount, BLOCK_AMOUNT);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardDiscarded.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardDiscarded.Unsubscribe(this);
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
            if (amount <= 0)
            {
                return;
            }

            GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
            reducePower(1);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.UpgradeFromHand(name, UPGRADE_CARDS_AMOUNT, true)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    SFX.Play(SFX.CARD_UPGRADE, 1f, 1.1f);
                }
            });
        }
    }
}
package pinacolada.cards.pcl.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class WinryRockbell extends PCLCard
{
    public static final PCLCardData DATA = Register(WinryRockbell.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int UPGRADE_CARDS_AMOUNT = 2;
    public static final int BLOCK_AMOUNT = 4;


    public WinryRockbell()
    {
        super(DATA);

        Initialize(0, 0, BLOCK_AMOUNT, UPGRADE_CARDS_AMOUNT);
        SetUpgrade(0, 0);

        SetAffinity_Green(1);
        SetAffinity_Orange(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new WinryRockbellPower(p, 1));
    }

    public static class WinryRockbellPower extends PCLClickablePower implements OnAfterCardDiscardedSubscriber
    {
        public WinryRockbellPower(AbstractCreature owner, int amount)
        {
            super(owner, WinryRockbell.DATA, PowerTriggerConditionType.Affinity, 6);

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

            PCLCombatStats.onAfterCardDiscarded.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onAfterCardDiscarded.Unsubscribe(this);
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

            PCLActions.Bottom.GainBlock(BLOCK_AMOUNT);
            reducePower(1);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.UpgradeFromHand(name, UPGRADE_CARDS_AMOUNT, true)
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
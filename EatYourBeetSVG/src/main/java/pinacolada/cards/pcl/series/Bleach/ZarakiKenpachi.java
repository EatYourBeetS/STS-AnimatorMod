package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import eatyourbeets.interfaces.subscribers.OnBlockBrokenSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.utilities.PCLActions;

public class ZarakiKenpachi extends PCLCard
{
    public static final PCLCardData DATA = Register(ZarakiKenpachi.class).SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage();
    public static final int MODIFIER_INCREASE = 2;
    public static final int MODIFIER_DECREASE = -1;

    public ZarakiKenpachi()
    {
        super(DATA);

        Initialize(0, 0, 4, MODIFIER_INCREASE);
        SetUpgrade(0, 0, 2);
        SetAffinity_Red(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new ZarakiKenpachiPower(p, magicNumber));
    }

    public static class ZarakiKenpachiPower extends PCLPower implements OnBlockBrokenSubscriber, OnStartOfTurnPostDrawSubscriber
    {
        public static final String POWER_ID = CreateFullID(ZarakiKenpachiPower.class);
        boolean activated;

        public ZarakiKenpachiPower(AbstractPlayer owner, int amount)
        {
            super(owner, ZarakiKenpachi.DATA);

            this.amount = amount;

            for (AbstractPCLAffinityPower po : PCLCombatStats.MatchingSystem.Powers) {
                if (PCLAffinity.Red.equals(po.affinity)) {
                    po.SetGainMultiplier(po.gainMultiplier + 1);
                }
                else {
                    po.SetEnabled(false);
                }
            }

            PCLCombatStats.onBlockBroken.Subscribe(this);
            PCLCombatStats.onStartOfTurnPostDraw.Subscribe(this);

            updateDescription();
        }

        @Override
        public void OnStartOfTurnPostDraw()
        {
            activated = false;
            updateDescription();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (AbstractPCLAffinityPower po : PCLCombatStats.MatchingSystem.Powers) {
                if (PCLAffinity.Red.equals(po.affinity)) {
                    po.SetGainMultiplier(po.gainMultiplier - 1);
                }
                else {
                    po.SetEnabled(true);
                }
            }

            PCLCombatStats.onBlockBroken.Unsubscribe(this);
            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }

        @Override
        public void OnBlockBroken(AbstractCreature creature)
        {
            if (!creature.isPlayer && !activated)
            {
                activated = true;
                PCLActions.Bottom.GainStrength(amount);
                PCLActions.Bottom.ApplyPower(new LoseStrengthPower(player, amount));
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}
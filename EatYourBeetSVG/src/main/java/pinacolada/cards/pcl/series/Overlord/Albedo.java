package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.powers.common.EnchantedArmorPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Albedo extends PCLCard
{
    public static final PCLCardData DATA = Register(Albedo.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Albedo()
    {
        super(DATA);

        Initialize(0, 0, 2, 12);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(2, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        PCLActions.Bottom.GainArtifact(1);
        PCLActions.Bottom.StackPower(new AlbedoPower(p, magicNumber));
    }

    public static class AlbedoPower extends PCLClickablePower implements OnChannelOrbSubscriber
    {
        public static final int EXHAUST_AMOUNT = 1;
        protected int darkAmount = 0;

        public AlbedoPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA, PowerTriggerConditionType.Affinity, -1, null, null, PCLAffinity.Dark);

            Initialize(amount);
            this.triggerCondition.SetCheckCondition((c) -> {
                return PCLJUtils.Count(AbstractDungeon.player.hand.group, card -> card.type.equals(CardType.ATTACK)) >= EXHAUST_AMOUNT;
            })
                    .SetPayCost(cost -> {
                        PCLActions.Bottom.ExhaustFromHand(name, EXHAUST_AMOUNT, false).SetOptions(false, false, false).SetFilter(card -> card.type.equals(CardType.ATTACK));
                    });
            this.triggerCondition.SetOneUsePerPower(true);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.SelectFromHand(name,1,false).AddCallback(cards -> {
                for (AbstractCard card: cards) {
                    PCLGameUtilities.IncreaseDamage(card, cost,false);
                }
            });
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, darkAmount);
        }

        protected void updateCount(AbstractOrb orb) {
            if (Dark.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.AddAffinity(PCLAffinity.Dark, amount);
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            updateCount(orb);
        }

        @Override
        public void OnChannelOrb(AbstractOrb orb) {
            updateCount(orb);
        }
    }
}
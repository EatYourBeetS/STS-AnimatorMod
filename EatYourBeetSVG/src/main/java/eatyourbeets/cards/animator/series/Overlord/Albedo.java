package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Albedo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Albedo.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Albedo()
    {
        super(DATA);

        Initialize(0, 0, 2, 18);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Blue(2, 0, 0);
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
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, magicNumber));
        GameActions.Bottom.GainTemporaryArtifact(1);
        GameActions.Bottom.StackPower(new AlbedoPower(p, magicNumber));
    }

    public static class AlbedoPower extends AnimatorClickablePower implements OnChannelOrbSubscriber
    {
        public static final int EXHAUST_AMOUNT = 1;
        protected int darkAmount = 0;

        public AlbedoPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA, PowerTriggerConditionType.Exhaust, EXHAUST_AMOUNT);

            Initialize(amount);
            this.triggerCondition.SetCheckCondition((c) -> {
                return JUtils.Count(AbstractDungeon.player.hand.group, card -> card.type == CardType.ATTACK) >= EXHAUST_AMOUNT;
            })
                    .SetPayCost(cost -> {
                        GameActions.Bottom.ExhaustFromHand(name, cost, false).SetOptions(false, false, false).SetFilter(card -> card.type == CardType.ATTACK);
                    });
            this.triggerCondition.SetOneUsePerPower(true);
            updateCount();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.SelectFromHand(name,1,false).AddCallback(cards -> {
                for (AbstractCard card: cards) {
                    GameUtilities.ModifyDamage(card,CombatStats.Affinities.GetHandAffinityLevel(Affinity.Dark,null),false);
                }
            });
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, darkAmount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onChannelOrb.Subscribe(this);
            updateCount();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateCount();
        }

        @Override
        public void reducePower(int reduceAmount)
        {
            super.reducePower(reduceAmount);
            updateCount();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.Affinities.BonusAffinities.Add(Affinity.Dark, -darkAmount);
        }

        protected void updateCount() {
            CombatStats.Affinities.BonusAffinities.Add(Affinity.Dark, -darkAmount);
            darkAmount = Math.max(amount,GameUtilities.GetOrbCount(Dark.ORB_ID));
            CombatStats.Affinities.BonusAffinities.Add(Affinity.Dark, darkAmount);
            updateDescription();
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb) {

            super.onEvokeOrb(orb);

            updateCount();
        }

        @Override
        public void OnChannelOrb(AbstractOrb orb) {
            updateCount();
        }
    }
}
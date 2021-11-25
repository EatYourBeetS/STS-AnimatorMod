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
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.common.EnchantedArmorPower;
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

        Initialize(0, 0, 2, 12);
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
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        GameActions.Bottom.GainArtifact(1);
        GameActions.Bottom.StackPower(new AlbedoPower(p, magicNumber));
    }

    public static class AlbedoPower extends AnimatorClickablePower implements OnChannelOrbSubscriber
    {
        public static final int EXHAUST_AMOUNT = 1;
        protected int darkAmount = 0;

        public AlbedoPower(AbstractCreature owner, int amount)
        {
            super(owner, Albedo.DATA, PowerTriggerConditionType.Affinity, -1, null, null, Affinity.Dark);

            Initialize(amount);
            this.triggerCondition.SetCheckCondition((c) -> {
                return JUtils.Count(AbstractDungeon.player.hand.group, card -> card.type.equals(CardType.ATTACK)) >= EXHAUST_AMOUNT;
            })
                    .SetPayCost(cost -> {
                        GameActions.Bottom.ExhaustFromHand(name, EXHAUST_AMOUNT, false).SetOptions(false, false, false).SetFilter(card -> card.type.equals(CardType.ATTACK));
                    });
            this.triggerCondition.SetOneUsePerPower(true);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.SelectFromHand(name,1,false).AddCallback(cards -> {
                for (AbstractCard card: cards) {
                    GameUtilities.IncreaseDamage(card, cost,false);
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
                GameActions.Bottom.AddAffinity(Affinity.Dark, amount);
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
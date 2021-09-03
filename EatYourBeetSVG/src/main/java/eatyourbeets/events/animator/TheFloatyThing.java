package eatyourbeets.events.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.SpiritPoop;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class TheFloatyThing extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheFloatyThing.class);
    private static final int PRICE = 16;
    private static int rolls = 0;
    private static boolean canDemand = false;
    private static boolean hasDemanded = false;

    public static TheFloatyThing TryCreate(Random rng)
    {
        if (rolls >= 0 && rng.randomBoolean(0.15f)) {
            return new TheFloatyThing();
        }
        return null;
    }

    private static float GetChance() {
        return rolls >= 89 ? 1f : rolls >= 75 ? 0.324f : 0.06f;
    }

    private static int GetGoldToRefund() {return rolls * PRICE / 2;}

    public TheFloatyThing()
    {
        super(ID, new EventStrings(),"PaimonsBargains.png");
        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Introduction2());
        RegisterPhase(2, new Offering());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Introduction2 extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction2());
            AddContinueOption();
        }
    }

    private static class Offering extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        private String merchantLine;

        @Override
        protected void OnEnter()
        {
            if (merchantLine == null)
            {
                merchantLine = text.Offering();
            }

            AddText(merchantLine);

            boolean hasEnoughGold = (player.gold >= PRICE);

            if (hasEnoughGold)
            {
                AddOption(text.WishOption(GetChance(), PRICE)).AddCallback(this::Wish);
            }
            else
            {
                AddOption(text.WishLockedOption()).SetDisabled(true);
            }
            if (canDemand && !hasDemanded) {
                AddOption(text.DemandOption(GetGoldToRefund())).AddCallback(this::Demand);
            }
            AddLeaveOption();
        }

        private void Wish()
        {
            player.loseGold(PRICE);
            rolls += 1;

            if (MathUtils.randomBoolean(GetChance())) {
                ObtainReward();
            }
            else {
                if (rolls % 10 == 0) {
                    SpiritPoop relic = new SpiritPoop();
                    relic.instantObtain();
                    CardCrawlGame.metricData.addRelicObtainData(relic);
                    merchantLine = text.Offering3(rolls);
                    canDemand = true;
                }
                else {
                    merchantLine = text.Offering2(rolls);
                }
                Refresh();
            }
        }

        private void Demand()
        {
            if (MathUtils.randomBoolean()) {
                ChangePhase(Refund.class);
            }
            else {
                merchantLine = text.Offering4(rolls);
                hasDemanded = true;
                Refresh();
            }
        }

        private void ObtainReward()
        {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            RewardItem rewardItem = new RewardItem(GR.Animator.CardColor);
            RandomizedList<AbstractCard> cards = new RandomizedList<>(JUtils.Filter(CardLibrary.getAllCards(), c -> c instanceof AnimatorCard_UltraRare));

            room.rewards.clear();
            rewardItem.cards.clear();
            for (int i = 0; i < 3; i++)
            {
                AbstractCard card = cards.Retrieve(RNG).makeCopy();
                card.upgrade();
                rewardItem.cards.add(card);
            }

            room.addCardReward(rewardItem);
            AbstractDungeon.combatRewardScreen.open();
            ChangePhase(Leave1.class);

        }
    }

    private static class Leave1 extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Leave1());
            AddContinueOption();
        }
    }

    private static class Leave2 extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Leave2());
            AddLeaveOption();
        }
    }

    private static class Refund extends EYBEventPhase<TheFloatyThing, TheFloatyThing.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            player.gainGold(PRICE * rolls / 2);
            AddText(text.Refund());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public final String Introduction()
        {
            return GetDescription(0);
        }

        public final String Introduction2()
        {
            return GetDescription(1);
        }

        public final String Offering()
        {
            return GetDescription(2);
        }

        public final String Offering2(int rolls)
        {
            return GetDescription(3, rolls);
        }

        public final String Offering3(int rolls)
        {
            return GetDescription(4, rolls);
        }

        public final String Offering4(int rolls)
        {
            return GetDescription(5, rolls);
        }

        public final String Refund()
        {
            return GetDescription(6);
        }

        public final String Leave1()
        {
            return GetDescription(7);
        }

        public final String Leave2()
        {
            return GetDescription(8);
        }

        public final String WishOption(float chance, int gold)
        {
            return GetOption(0, chance, gold);
        }

        public final String WishLockedOption()
        {
            return GetOption(1);
        }

        public final String DemandOption(int gold)
        {
            return GetOption(2);
        }

        public final String RefuseBountyOption()
        {
            return GetOption(4);
        }
    }
}
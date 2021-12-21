package pinacolada.events.pcl;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import pinacolada.cards.pcl.special.BlazingHeat;
import pinacolada.cards.pcl.special.IonizingStorm;
import pinacolada.cards.pcl.special.SheerCold;
import pinacolada.events.base.PCLEvent;
import pinacolada.relics.pcl.SpiritPoop2;
import pinacolada.relics.pcl.SpiritPoop3;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class TheFloatyThing extends PCLEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheFloatyThing.class);
    private static final int PRICE = 16;
    private static int rolls = 0;
    private static boolean canDemand = false;
    private static boolean hasDemanded = false;

    public static TheFloatyThing TryCreate(Random rng)
    {
        if (AbstractDungeon.floorNum > 14 && PCLGameUtilities.GetGold() > 160 && !(PCLGameUtilities.HasRelic(SpiritPoop3.ID)) && rng.randomBoolean(0.15f)) {
            return new TheFloatyThing();
        }
        return null;
    }

    private static float GetChance() {
        return rolls >= 89 ? 1f : rolls >= 75 ? 0.324f : 0.006f;
    }

    private static int GetGoldToRefund() {return rolls * PRICE;}

    public TheFloatyThing()
    {
        super(ID, new EventStrings(),IMAGES.PaimonsBargains.Path());
        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Introduction2());
        RegisterPhase(2, new Offering());
        RegisterPhase(3, new Leave1());
        RegisterPhase(4, new Leave2());
        RegisterPhase(5, new Refund());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheFloatyThing, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            rolls = 0;
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
                AddOption(text.WishOption(GetChance() * 100f, PRICE)).AddCallback(this::Wish);
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
                    if (MathUtils.randomBoolean()) {
                        AbstractRelic relic = MathUtils.randomBoolean(0.25f) ? AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE) : AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.UNCOMMON);
                        relic.instantObtain();
                        CardCrawlGame.metricData.addRelicObtainData(relic);
                        merchantLine = text.WishSuccess(rolls);
                    }
                    else {
                        SpiritPoop2 relic = new SpiritPoop2();
                        relic.instantObtain();
                        CardCrawlGame.metricData.addRelicObtainData(relic);
                        merchantLine = text.WishKindaSuccess(rolls);
                        canDemand = true;
                    }
                }
                else {
                    merchantLine = text.WishFail(rolls);
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
                merchantLine = text.WishRefundFail(rolls);
                hasDemanded = true;
                Refresh();
            }
        }

        private void ObtainReward()
        {
            SpiritPoop3 relic = new SpiritPoop3();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            RewardItem rewardItem = new RewardItem(GR.PCL.CardColor);

            room.rewards.clear();
            rewardItem.cards.clear();
            rewardItem.cards.add(new BlazingHeat());
            rewardItem.cards.add(new SheerCold());
            rewardItem.cards.add(new IonizingStorm());

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
            PCLGameEffects.List.Add(new RainingGoldEffect(600));
            player.gainGold(GetGoldToRefund());
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

        public final String WishFail(int rolls)
        {
            return GetDescription(3, rolls);
        }

        public final String WishSuccess(int rolls)
        {
            return GetDescription(4, rolls);
        }

        public final String WishKindaSuccess(int rolls)
        {
            return GetDescription(5, rolls);
        }

        public final String WishRefundFail(int rolls)
        {
            return GetDescription(6, rolls);
        }

        public final String Refund()
        {
            return GetDescription(7);
        }

        public final String Leave1()
        {
            return GetDescription(8);
        }

        public final String Leave2()
        {
            return GetDescription(9);
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
            return GetOption(2, gold);
        }

        public final String RefuseBountyOption()
        {
            return GetOption(4);
        }
    }
}
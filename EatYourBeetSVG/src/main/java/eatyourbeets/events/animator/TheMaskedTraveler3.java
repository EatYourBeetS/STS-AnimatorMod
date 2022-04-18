package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class TheMaskedTraveler3 extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheMaskedTraveler3.class);

    public TheMaskedTraveler3()
    {
        super(ID, STRINGS, IMAGES.MaskedTraveler.Path());

        this.noCardsInRewards = true;

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Farewell());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheMaskedTraveler3, EventStrings>
    {
        private static final int PRICE = 150;

        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());

            if (player.hasRelic(AncientMedallion.ID))
            {
                AddOption(text.TradeMedallionOption()).AddCallback(this::TradeMedallion);
            }
            else
            {
                AddOption(text.TradeMedallionLockedOption()).SetDisabled(true);
            }

            if (player.gold >= PRICE)
            {
                AddOption(text.TradeGoldOption(PRICE)).AddCallback(this::TradeGold);
            }
            else
            {
                AddOption(text.TradeGoldLockedOption()).SetDisabled(true);
            }

            AddLeaveOption();
        }

        private void TradeMedallion()
        {
            AncientMedallion medallion = GameUtilities.GetRelic(AncientMedallion.class);
            if (medallion != null)
            {
                medallion.setCounter(medallion.counter - 1);
                ObtainReward();
            }

            ProgressPhase();
        }

        private void TradeGold()
        {
            player.loseGold(PRICE);
            ObtainReward();

            ProgressPhase();
        }

        private void ObtainReward()
        {
            final AbstractRoom room = AbstractDungeon.getCurrRoom();
            final RewardItem rewardItem = new RewardItem(GR.Animator.CardColor);
            final RandomizedList<AbstractCard> cards = new RandomizedList<>(AbstractDungeon.rareCardPool.group);

            room.rewards.clear();
            rewardItem.cards.clear();
            for (int i = 0; i < 3; i++)
            {
                AbstractCard card = cards.Retrieve(RNG).makeCopy();
                card.upgrade();
                rewardItem.cards.add(card);
            }

            room.addCardReward(rewardItem);
            room.phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.combatRewardScreen.open();
        }
    }

    private static class Farewell extends EYBEventPhase<TheMaskedTraveler1, EventStrings>
    {
        @Override
        public void OnEnter()
        {
            AddText(text.Farewell(), true);
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public final String Introduction()
        {
            return GetDescription(0);
        }

        public final String Farewell()
        {
            return GetDescription(1);
        }

        public final String TradeMedallionOption()
        {
            return GetOption(0);
        }

        public final String TradeGoldOption(int gold)
        {
            return GetOption(1, gold);
        }

        public final String TradeMedallionLockedOption()
        {
            return GetOption(2);
        }

        public final String TradeGoldLockedOption()
        {
            return GetOption(3);
        }
    }
}
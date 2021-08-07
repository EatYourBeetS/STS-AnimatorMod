package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class TheMaskedTraveler1 extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheMaskedTraveler1.class);

    public TheMaskedTraveler1()
    {
        super(ID, new EventStrings());

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Farewell());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheMaskedTraveler1, EventStrings>
    {
        private final int SELLING_PRICE = RNG.random(70, 90);
        private final int BUYING_PRICE = RNG.random(90, 110);

        @Override
        public void OnEnter()
        {
            RandomizedList<AbstractCard> cards = new RandomizedList<>();

            for (AbstractCard card : player.masterDeck.group)
            {
                if (card.tags.contains(GR.Enums.CardTags.IMPROVED_BASIC_CARD))
                {
                    cards.Add(card);
                }
            }

            AbstractCard card = cards.Retrieve(RNG);
            if (card != null)
            {
                AddOption(text.SellCardOption(card.name, SELLING_PRICE), card).AddCallback(this::SellCard);
            }
            else
            {
                AddOption(text.SellCardLockedOption()).SetDisabled(true);
            }

            if (player.gold >= BUYING_PRICE)
            {
                AddOption(text.ImproveCardsOption(BUYING_PRICE)).AddCallback(this::ImproveCards);
            }
            else
            {
                AddOption(text.ImproveCardsLockedOption()).SetDisabled(true);
            }

            AddLeaveOption();
        }

        private void SellCard(EYBEventOption option)
        {
            GameEffects.List.Add(new RainingGoldEffect(SELLING_PRICE));
            player.masterDeck.removeCard(option.card);
            player.gainGold(SELLING_PRICE);
            ProgressPhase();
        }

        private void ImproveCards(EYBEventOption option)
        {
            player.loseGold(BUYING_PRICE);

            final RandomizedList<AbstractCard> strikes = new RandomizedList<>();
            final RandomizedList<AbstractCard> defends = new RandomizedList<>();
            for (AbstractCard c : player.masterDeck.group)
            {
                if (!c.hasTag(GR.Enums.CardTags.IMPROVED_BASIC_CARD))
                {
                    if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
                    {
                        strikes.Add(c);
                    }
                    else if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
                    {
                        defends.Add(c);
                    }
                }
            }

            final EYBCardData defend = GameUtilities.GetRandomElement(ImprovedDefend.GetCards(), RNG);
            final EYBCardData strike = GameUtilities.GetRandomElement(ImprovedStrike.GetCards(), RNG);
            if (strikes.Size() > 0 && strike != null)
            {
                SwapCards(strike, strikes.Retrieve(RNG), defend == null ? 0.5f : 0.4f);
            }
            if (defends.Size() > 0 && defend != null)
            {
                SwapCards(defend, defends.Retrieve(RNG), strike == null ? 0.5f : 0.6f);
            }

            ProgressPhase();
        }

        private void SwapCards(EYBCardData toAdd, AbstractCard toRemove, float offset_x)
        {
            player.masterDeck.group.remove(toRemove);
            GameEffects.List.ShowAndObtain(toAdd.MakeCopy(toRemove.upgraded), Settings.WIDTH * offset_x, (float) Settings.HEIGHT / 2f, false);
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

        public final String SellCardOption(String card, int gold)
        {
            return GetOption(0, card, gold);
        }

        public final String ImproveCardsOption(int gold)
        {
            return GetOption(1, gold);
        }

        public final String SellCardLockedOption()
        {
            return GetOption(2);
        }

        public final String ImproveCardsLockedOption()
        {
            return GetOption(3);
        }
    }
}
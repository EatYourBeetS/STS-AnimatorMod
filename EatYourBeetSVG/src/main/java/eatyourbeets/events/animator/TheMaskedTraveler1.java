package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventOption;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

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
        private final ArrayList<AbstractCard> replacementStrikes = new ArrayList<>();
        private final ArrayList<AbstractCard> replacementDefends = new ArrayList<>();
        private final int SELLING_PRICE = RNG.random(30, 50);
        private final int BUYING_PRICE = RNG.random(85, 100);

        @Override
        public void OnEnter()
        {
            RandomizedList<AbstractCard> cards = new RandomizedList<>();

            for (AbstractCard card : player.masterDeck.group)
            {
                if (card.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND) || card.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE))
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

            if (replacementStrikes.isEmpty())
            {
                for (AbstractCard c : CardLibrary.getAllCards())
                {
                    if (c.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE))
                    {
                        replacementStrikes.add(c);
                    }
                    else if (c.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND))
                    {
                        replacementDefends.add(c);
                    }
                }
            }

            RandomizedList<AbstractCard> strikes = new RandomizedList<>(replacementStrikes);
            RandomizedList<AbstractCard> defends = new RandomizedList<>(replacementDefends);
            ArrayList<AbstractCard> deck = player.masterDeck.group;
            ArrayList<AbstractCard> strikesToReplace = new ArrayList<>();
            ArrayList<AbstractCard> defendsToReplace = new ArrayList<>();

            for (AbstractCard card : deck)
            {
                if (card.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE) || card.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND))
                {
                    if (card.tags.contains(AbstractCard.CardTags.HEALING))
                    {
                        strikes.GetInnerList().removeIf(c -> c.cardID.equals(card.cardID));
                    }
                }
                else if (card.tags.contains(AbstractCard.CardTags.STARTER_DEFEND))
                {
                    defendsToReplace.add(card);
                }
                else if (card.tags.contains(AbstractCard.CardTags.STARTER_STRIKE))
                {
                    strikesToReplace.add(card);
                }
            }

            for (AbstractCard card : strikesToReplace)
            {
                deck.remove(card);
                ObtainCard(strikes.Retrieve(RNG), card.upgraded);
            }

            for (AbstractCard card : defendsToReplace)
            {
                deck.remove(card);
                ObtainCard(defends.Retrieve(RNG), card.upgraded);
            }

            ProgressPhase();
        }

        private void ObtainCard(AbstractCard replacement, boolean upgraded)
        {
            replacement = replacement.makeCopy();

            if (upgraded)
            {
                replacement.upgrade();
            }

            GameEffects.List.Add(new ShowCardAndObtainEffect(replacement, (float) Settings.WIDTH / 2f, (float) Settings.HEIGHT / 2f));
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
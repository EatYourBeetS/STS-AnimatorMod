package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EventOption;
import eatyourbeets.events.base.EventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class TheMaskedTraveler1_New extends EYBEvent
{
    public static final String ID = AnimatorEvent.CreateFullID(TheMaskedTraveler1_New.class.getSimpleName());

    public TheMaskedTraveler1_New()
    {
        super(ID, new EventStrings());

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Farewell());
        ChangePhase(0);
    }

    private static class Introduction extends EventPhase<TheMaskedTraveler1_New, EventStrings>
    {
        private final ArrayList<AbstractCard> replacementStrikes = new ArrayList<>();
        private final ArrayList<AbstractCard> replacementDefends = new ArrayList<>();
        private final int SELLING_PRICE = AbstractDungeon.miscRng.random(30, 50);
        private final int BUYING_PRICE = AbstractDungeon.miscRng.random(85, 100);

        @Override
        public void OnEnter()
        {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard card : p.masterDeck.group)
            {
                if (card.tags.contains(GR.Enums.CardTags.IMPROVED_DEFEND) || card.tags.contains(GR.Enums.CardTags.IMPROVED_STRIKE))
                {
                    cards.add(card);
                }
            }

            AbstractCard card = JavaUtilities.GetRandomElement(cards);
            if (card != null)
            {
                SetOption(0, text.SellCardOption(card.name, SELLING_PRICE), card).AddCallback(this::SellCard);
            }
            else
            {
                SetOption(0, text.SellCardLockedOption()).SetDisabled(true);
            }

            if (p.gold >= BUYING_PRICE)
            {
                SetOption(1, text.ImproveCardsOption(BUYING_PRICE)).AddCallback(this::ImproveCards);
            }
            else
            {
                SetOption(1, text.ImproveCardsLockedOption()).SetDisabled(true);
            }

            SetOption(2, text.LeaveOption()).AddCallback(event::OpenMap);
        }

        private void SellCard(EventOption option)
        {
            GameEffects.List.Add(new RainingGoldEffect(SELLING_PRICE));
            player.masterDeck.removeCard(option.card);
            player.gainGold(SELLING_PRICE);

            event.ProgressPhase();
        }

        private void ImproveCards(EventOption option)
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
                ObtainCard(strikes.Retrieve(AbstractDungeon.miscRng), card.upgraded);
            }

            for (AbstractCard card : defendsToReplace)
            {
                deck.remove(card);
                ObtainCard(defends.Retrieve(AbstractDungeon.miscRng), card.upgraded);
            }

            event.ProgressPhase();
        }

        private void ObtainCard(AbstractCard replacement, boolean upgraded)
        {
            replacement = replacement.makeCopy();

            if (upgraded)
            {
                replacement.upgrade();
            }

            GameEffects.List.Add(new ShowCardAndObtainEffect(replacement, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }

    private static class Farewell extends EventPhase<TheMaskedTraveler1_New, EventStrings>
    {
        @Override
        public void OnEnter()
        {
            SetText(text.Farewell(), true);
            SetOption(0, text.LeaveOption()).AddCallback(event::OpenMap);
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

        public final String LeaveOption()
        {
            return GetOption(2);
        }

        public final String SellCardLockedOption()
        {
            return GetOption(3);
        }

        public final String ImproveCardsLockedOption()
        {
            return GetOption(4);
        }
    }
}
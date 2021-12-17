package pinacolada.events.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.pcl.curse.Curse_Dizziness;
import pinacolada.cards.pcl.special.HinaKagiyama;
import pinacolada.events.base.PCLEvent;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class TheCursedForest extends PCLEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheCursedForest.class);

    public static TheCursedForest TryCreate(Random rng)
    {
        int curseCount = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
        {
            if (card.cardID.equals(HinaKagiyama.DATA.ID))
            {
                return null;
            }
            else if (card.type == AbstractCard.CardType.CURSE)
            {
                curseCount += 1;
            }
        }

        return rng.randomBoolean(0.03f + (curseCount * 0.02f)) ? new TheCursedForest() : null;
    }

    public TheCursedForest()
    {
        super(ID, STRINGS, IMAGES.CursedForest.Path());

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Offer());
        RegisterPhase(2, new Farewell());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheCursedForest, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Offer extends EYBEventPhase<TheCursedForest, EventStrings>
    {
        private final static int CURSES = 2;
        private final AbstractCard card = new HinaKagiyama();
        private final AbstractCard curse = new Curse_Dizziness();
        private String OfferLine;

        @Override
        protected void OnEnter()
        {
            if (OfferLine == null)
            {
                OfferLine = text.Offering();
            }

            AddText(OfferLine);
            AddOption(text.EmbraceOption(), card).AddCallback(this::Embrace);
            AddOption(text.PurifyOption(CURSES)).AddCallback(this::Purify);
        }

        private void Embrace()
        {
            PCLGameEffects.List.ShowAndObtain(card, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2f, false);
            PCLGameEffects.List.ShowAndObtain(curse, (float) Settings.WIDTH * 0.55f, (float) Settings.HEIGHT / 2f, false);
            ProgressPhase();
        }

        private void Purify()
        {
            RandomizedList<AbstractCard> toRemove = new RandomizedList<>();
            for (AbstractCard card : player.masterDeck.group)
            {
                if (card.type == AbstractCard.CardType.CURSE && PCLGameUtilities.CanRemoveFromDeck(card))
                {
                    toRemove.Add(card);
                }
            }

            if (toRemove.Size() > 0)
            {
                RemoveCard(toRemove.Retrieve(RNG), true);

                if (toRemove.Size() > 0)
                {
                    RemoveCard(toRemove.Retrieve(RNG), false);
                }
            }

            ProgressPhase();
        }
    }

    private static class Farewell extends EYBEventPhase<TheCursedForest, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Farewell());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public String Introduction()
        {
            return GetDescription(0);
        }

        public String Offering()
        {
            return GetDescription(1);
        }

        public String Farewell()
        {
            return GetDescription(2);
        }

        public String EmbraceOption()
        {
            return GetOption(0);
        }

        public String PurifyOption(int curses)
        {
            return GetOption(1, curses);
        }
    }
}
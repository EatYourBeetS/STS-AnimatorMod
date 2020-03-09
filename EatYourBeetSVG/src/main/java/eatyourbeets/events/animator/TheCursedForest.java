package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.curse.Curse_Dizziness;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class TheCursedForest extends EYBEvent
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

        switch (curseCount)
        {
            case  0: return null;
            case  1: return rng.randomBoolean(0.10f) ? new TheCursedForest() : null;
            case  2: return rng.randomBoolean(0.25f) ? new TheCursedForest() : null;
            case  3: return rng.randomBoolean(0.33f) ? new TheCursedForest() : null;
            default: return rng.randomBoolean(0.60f) ? new TheCursedForest() : null;
        }
    }

    public TheCursedForest()
    {
        super(ID, STRINGS, "CursedForest.png");

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
            GameEffects.List.Add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH * 0.45f, (float) Settings.HEIGHT / 2.0F));
            GameEffects.List.Add(new ShowCardAndObtainEffect(curse, (float) Settings.WIDTH * 0.55f, (float) Settings.HEIGHT / 2.0F));
            ProgressPhase();
        }

        private void Purify()
        {
            RandomizedList<AbstractCard> toRemove = new RandomizedList<>();
            for (AbstractCard card : player.masterDeck.group)
            {
                if (card.type == AbstractCard.CardType.CURSE && GameUtilities.CanRemoveFromDeck(card))
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
package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.cards.animator.curse.Curse_Dizziness;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class TheCursedForest extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheCursedForest.class);

    public TheCursedForest()
    {
        super(ID, STRINGS, "Merchant.png");

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
        private String OfferLine;

        @Override
        protected void OnEnter()
        {
            if (OfferLine == null)
            {
                OfferLine = text.Offering();
            }

            AddText(OfferLine);


            AddOption(text.EmbraceOption(), new HinaKagiyama()).AddCallback(this::Embrace);
            AddOption(text.PurifyOption()).AddCallback(this::Purify);
        }
        private void Embrace(){
            player.masterDeck.addToBottom(new HinaKagiyama());
            player.masterDeck.addToBottom(new Curse_Dizziness());
        }
        private void Purify(){

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
        return GetDescription(4);
    }

    public String EmbraceOption()
    {
        return GetOption(0);
    }

    public String PurifyOption()
    {
        return GetOption(1);
    }
}
}
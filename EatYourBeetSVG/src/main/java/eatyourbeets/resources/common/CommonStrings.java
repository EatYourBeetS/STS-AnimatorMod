package eatyourbeets.resources.common;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

public class CommonStrings
{
    public CardBadges CardBadges;
    public TheUnnamedReign TheUnnamedReign;
    public HandSelection HandSelection;
    public GridSelection GridSelection;

    public void Initialize()
    {
        CardBadges = new CardBadges();
        TheUnnamedReign = new TheUnnamedReign();
        HandSelection = new HandSelection();
        GridSelection = new GridSelection();
    }

    public class CardBadges
    {
        public final UIStrings Strings = GetUIStrings("CardBadges");
        public final String Tooltip = Strings.TEXT[0];

        public final String GetDescription(int id)
        {
            return Strings.TEXT[id + 1];
        }

        public final String GetName(int id)
        {
            return Strings.EXTRA_TEXT[id];
        }
    }

    public class TheUnnamedReign
    {
        public final UIStrings Strings = GetUIStrings("TheUnnamedReign");
        public final String Name = Strings.TEXT[0];
    }

    public class HandSelection
    {
        public final UIStrings Strings = GetUIStrings("HandSelection");
        public final String MoveToDrawPile = Strings.TEXT[0];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Choose = CardRewardScreen.TEXT[1];
    }

    public class GridSelection
    {
        public final UIStrings Strings = GetUIStrings("GridSelection");
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Cycle = GamblingChipAction.TEXT[1];
        public final String Choose = CardRewardScreen.TEXT[1];

        public final String DiscardUpTo(int amount)
        {
            return JavaUtilities.Format(Strings.TEXT[0], amount);
        }

        public final String MoveToDrawPile(int amount)
        {
            return JavaUtilities.Format(Strings.TEXT[1], amount);
        }

        public final String TransformInto(String name)
        {
            return JavaUtilities.Format(Strings.TEXT[2], name);
        }
    }

    private static UIStrings GetUIStrings(String id)
    {
        String fullID = GR.CreateID(CommonResources.ID, id);

        return GR.GetUIStrings(fullID);
    }
}

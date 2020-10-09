package eatyourbeets.resources.common;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class CommonStrings
{
    public TheUnnamedReign TheUnnamedReign;
    public HandSelection HandSelection;
    public GridSelection GridSelection;

    public void Initialize()
    {
        TheUnnamedReign = new TheUnnamedReign();
        HandSelection = new HandSelection();
        GridSelection = new GridSelection();
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
        public final String DiscardUpTo = Strings.TEXT[0];
        public final String MoveToDrawPile = Strings.TEXT[1];
        public final String TransformInto = Strings.TEXT[2];
        public final String ChooseCards = Strings.TEXT[3];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Cycle = GamblingChipAction.TEXT[1];
        public final String Choose = CardRewardScreen.TEXT[1];

        public final String DiscardUpTo(int amount)
        {
            return JUtils.Format(DiscardUpTo, amount);
        }

        public final String MoveToDrawPile(int amount)
        {
            return JUtils.Format(MoveToDrawPile, amount);
        }

        public final String TransformInto(String name)
        {
            return JUtils.Format(TransformInto, name);
        }

        public final String ChooseCards(int amount)
        {
            return JUtils.Format(ChooseCards, amount);
        }
    }

    private static UIStrings GetUIStrings(String id)
    {
        String fullID = GR.CreateID(CommonResources.ID, id);

        return GR.GetUIStrings(fullID);
    }
}

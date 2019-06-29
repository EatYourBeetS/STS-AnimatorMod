package eatyourbeets.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class GridCardSelectScreenPatch
{
    private static final Field<Float> Field_drawStartX = Utilities.GetPrivateField("drawStartX", GridCardSelectScreen.class);
    private static final Field<Float> Field_padX = Utilities.GetPrivateField("padX", GridCardSelectScreen.class);
    private static final Field<Float> Field_padY = Utilities.GetPrivateField("padY", GridCardSelectScreen.class);
    private static final Field<Float> Field_drawStartY = Utilities.GetPrivateField("drawStartY", GridCardSelectScreen.class);
    private static final Field<Float> Field_currentDiffY = Utilities.GetPrivateField("currentDiffY", GridCardSelectScreen.class);
    private static final Field<AbstractCard> Field_hoveredCard = Utilities.GetPrivateField("hoveredCard", GridCardSelectScreen.class);
    private static final Field<CardGroup> Field_targetGroup = Utilities.GetPrivateField("targetGroup", GridCardSelectScreen.class);
    private static final Field<Integer> Field_prevDeckSize = Utilities.GetPrivateField("prevDeckSize", GridCardSelectScreen.class);
    private static final Field<Float> Field_scrollUpperBound = Utilities.GetPrivateField("scrollUpperBound", GridCardSelectScreen.class);

    private static CardGroup mergedGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static ArrayList<CardGroup> cardGroups = new ArrayList<>();
    private static boolean enabled = false;

    public static void Clear()
    {
        cardGroups.clear();
        mergedGroup.clear();
    }

    public static void AddGroup(CardGroup cardGroup)
    {
        if (!cardGroup.isEmpty())
        {
            cardGroups.add(cardGroup);
            mergedGroup.group.addAll(cardGroup.group);
        }

        enabled = !mergedGroup.isEmpty();
    }

    public static CardGroup GetCardGroup()
    {
        return mergedGroup;
    }

    public static void Open(GridCardSelectScreen selectScreen)
    {
        if (!enabled)
        {
            Clear();
        }
        else
        {
            enabled = false;
        }
    }

    public static boolean UpdateCardPositionAndHover(GridCardSelectScreen selectScreen)
    {
        if (cardGroups.isEmpty())
        {
            return false;
        }

        float lineNum = 0;

        float drawStartX = Field_drawStartX.Get(selectScreen);
        float drawStartY = Field_drawStartY.Get(selectScreen);
        float padX = Field_padX.Get(selectScreen);
        float padY = Field_padY.Get(selectScreen);
        float currentDiffY = Field_currentDiffY.Get(selectScreen);

        Field_hoveredCard.Set(selectScreen, null);

        for (CardGroup cardGroup : cardGroups)
        {
            ArrayList<AbstractCard> cards = cardGroup.group;
            for (int i = 0; i < cards.size(); ++i)
            {
                int mod = i % 5;
                if (mod == 0 && i != 0)
                {
                    lineNum += 1;
                }

                AbstractCard card = cards.get(i);

                card.target_x = drawStartX + (float) mod * padX;
                card.target_y = drawStartY + currentDiffY - lineNum * padY;
                card.fadingOut = false;
                card.stopGlowing();
                card.update();
                card.updateHoverLogic();

                if (card.hb.hovered)
                {
                    Field_hoveredCard.Set(selectScreen, card);
                }
            }

            lineNum += 1.3f;
        }

        return true;
    }

    public static boolean CalculateScrollBounds(GridCardSelectScreen instance)
    {
        if (cardGroups.isEmpty())
        {
            return false;
        }

        CardGroup targetCardGroup = Field_targetGroup.Get(instance);

        float scrollTmp = (mergedGroup.size() + 2.6f * cardGroups.size()) / 5f - 2;
        if (targetCardGroup.size() % 5 != 0)
        {
            scrollTmp += 1;
        }

        Field_scrollUpperBound.Set(instance, Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * Field_padY.Get(instance));
        Field_prevDeckSize.Set(instance, targetCardGroup.size());

        return true;
    }
}

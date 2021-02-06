package eatyourbeets.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class GridCardSelectScreenPatch
{
    private static final FieldInfo<Float> _drawStartX = JUtils.GetField("drawStartX", GridCardSelectScreen.class);
    private static final FieldInfo<Float> _padX = JUtils.GetField("padX", GridCardSelectScreen.class);
    private static final FieldInfo<Float> _padY = JUtils.GetField("padY", GridCardSelectScreen.class);
    private static final FieldInfo<Float> _drawStartY = JUtils.GetField("drawStartY", GridCardSelectScreen.class);
    private static final FieldInfo<Float> _currentDiffY = JUtils.GetField("currentDiffY", GridCardSelectScreen.class);
    private static final FieldInfo<AbstractCard> _hoveredCard = JUtils.GetField("hoveredCard", GridCardSelectScreen.class);
    private static final FieldInfo<CardGroup> _targetGroup = JUtils.GetField("targetGroup", GridCardSelectScreen.class);
    private static final FieldInfo<Integer> _prevDeckSize = JUtils.GetField("prevDeckSize", GridCardSelectScreen.class);
    private static final FieldInfo<Float> _scrollUpperBound = JUtils.GetField("scrollUpperBound", GridCardSelectScreen.class);

    private static final CardGroup mergedGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final ArrayList<CardGroup> cardGroups = new ArrayList<>();
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
            if (cardGroups.size() == 1)
            {
                _targetGroup.Set(selectScreen, cardGroups.get(0));
                Clear();
            }

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

        float drawStartX = _drawStartX.Get(selectScreen);
        float drawStartY = _drawStartY.Get(selectScreen);
        float padX = _padX.Get(selectScreen);
        float padY = _padY.Get(selectScreen);
        float currentDiffY = _currentDiffY.Get(selectScreen);

        _hoveredCard.Set(selectScreen, null);

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
                    _hoveredCard.Set(selectScreen, card);
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

        CardGroup targetCardGroup = _targetGroup.Get(instance);

        float scrollTmp = (mergedGroup.size() + 2.6f * cardGroups.size()) / 5f - 2;
        if (targetCardGroup.size() % 5 != 0)
        {
            scrollTmp += 1;
        }

        _scrollUpperBound.Set(instance, Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * _padY.Get(instance));
        _prevDeckSize.Set(instance, targetCardGroup.size());

        return true;
    }
}

package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class CardKeywordFilters extends GUIElement
{
    public static final HashSet<EYBCardTooltip> CurrentFilters = new HashSet<>();
    private final ArrayList<CardKeywordButton> filterButtons = new ArrayList<>();
    private final GUI_Button openButton;
    private final AdvancedHitbox hb;

    private boolean canShowFilters;
    private boolean shouldSortByCount;

    public CardKeywordFilters()
    {
        hb = new DraggableHitbox(ScreenW(0.15f), ScreenH(0.65f), Scale(140), Scale(50), false);
        for (Map.Entry<String, EYBCardTooltip> tooltipEntry : CardTooltips.GetEntries()) {
            filterButtons.add(new CardKeywordButton(hb, tooltipEntry.getValue()));
        }

        openButton = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, ScreenW(0.07f), ScreenH(0.07f)))
                .SetBorder(GR.Common.Images.HexagonalButton.Texture(), Color.WHITE)
                .SetPosition(Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.95f).SetText(GR.Animator.Strings.Misc.Filters)
                .SetOnClick(() -> canShowFilters = !canShowFilters);
    }

    public void Open(ArrayList<AbstractCard> cards, ActionT1<CardKeywordButton> onClick)
    {
        for (CardKeywordButton c : filterButtons)
        {
            c.SetOnClick(onClick).SetCardCount(cards);
        }

        Refresh();
    }

    public void Refresh()
    {

        filterButtons.sort((a, b) -> shouldSortByCount ? a.CardCount - b.CardCount : StringUtils.compare(a.Tooltip.title, b.Tooltip.title));

        int index = 0;
        for (CardKeywordButton c : filterButtons)
        {
            if (c.isActive)
            {
                c.SetIndex(index);
                index += 1;
            }
        }
    }

    @Override
    public void Update() {
        hb.update();
        openButton.TryUpdate();
        for (CardKeywordButton c : filterButtons)
        {
            c.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        openButton.TryRender(sb);
        if (canShowFilters) {
            hb.render(sb);
            for (CardKeywordButton c : filterButtons)
            {
                c.TryRender(sb);
            }
        }
    }
}

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
    private static final ArrayList<CardKeywordButton> filterButtons = new ArrayList<>();
    private static GUI_Button closeButton;
    private final AdvancedHitbox hb;

    private boolean shouldSortByCount;

    public CardKeywordFilters()
    {
        hb = new DraggableHitbox(ScreenW(0.15f), ScreenH(0.65f), Scale(180), Scale(50), false);
        if (filterButtons.isEmpty()) {
            for (Map.Entry<String, EYBCardTooltip> tooltipEntry : CardTooltips.GetEntries()) {
                filterButtons.add(new CardKeywordButton(hb, tooltipEntry.getValue()));
            }
        }
        closeButton = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f))
                .SetBorder(GR.Common.Images.HexagonalButton.Texture(), Color.WHITE)
                .SetPosition(Settings.WIDTH * 0.9f, Settings.HEIGHT * 0.95f).SetText("CLOSE")
                .SetOnClick(() -> {
                    //CardCrawlGame.isPopupOpen = false;
                    SetActive(false);
                });
    }

    public void Open() {
        //CardCrawlGame.isPopupOpen = true;
        SetActive(true);
    }

    public void Initialize(ArrayList<AbstractCard> cards, ActionT1<CardKeywordButton> onClick)
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
        closeButton.TryUpdate();
        for (CardKeywordButton c : filterButtons)
        {
            c.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        hb.render(sb);
        closeButton.TryRender(sb);
        for (CardKeywordButton c : filterButtons)
        {
            c.TryRender(sb);
        }
    }
}

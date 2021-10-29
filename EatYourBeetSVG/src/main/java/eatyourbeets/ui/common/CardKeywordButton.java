package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.animator.cardReward.CardAffinityPanel;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class CardKeywordButton extends GUIElement
{
    public static final int GRID_WIDTH = 8;
    private static final Color ACTIVE_COLOR = new Color(0.65f, 0.65f, 0.65f, 1f);
    private static final Color PANEL_COLOR = new Color(0.05f, 0.05f, 0.05f, 1f);
    private ActionT1<CardKeywordButton> onClick;

    public final EYBCardTooltip Tooltip;
    public final float baseCountOffset = -0.35f;
    public final float baseTextOffset = 0.15f;
    public int CardCount = -1;

    public GUI_Button background_button;
    public GUI_Label title_text;
    public GUI_Label count_text;

    public CardKeywordButton(Hitbox hb, EYBCardTooltip tooltip)
    {
        final float iconSize = CardAffinityPanel.ICON_SIZE;

        Tooltip = tooltip;

        background_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(CardKeywordFilters.CurrentFilters.contains(Tooltip) ? ACTIVE_COLOR : PANEL_COLOR)
        .SetText("")
                .SetOnClick(button -> {
                    if (CardKeywordFilters.CurrentFilters.contains(Tooltip))
                    {
                        CardKeywordFilters.CurrentFilters.remove(Tooltip);
                        background_button.SetColor(PANEL_COLOR);
                    }
                    else
                    {
                        CardKeywordFilters.CurrentFilters.add(Tooltip);
                        background_button.SetColor(ACTIVE_COLOR);
                    }

                    if (this.onClick != null) {
                        this.onClick.Invoke(this);
                    }
                });

        title_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 1f, 1, baseTextOffset, 0f))
        .SetAlignment(0.9f, 0.1f, true) // 0.1f
        .SetText((Tooltip.icon != null ? "[" + Tooltip.id + "] " : "") + Tooltip.title);

        count_text = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
                new RelativeHitbox(hb, 0.28f, 1, baseCountOffset, 0f))
                .SetAlignment(0.5f, 0.5f) // 0.1f
                .SetColor(Color.YELLOW)
                .SetText(CardCount);
    }

    public CardKeywordButton SetIndex(int index)
    {
        float x = (index % GRID_WIDTH) * 1.06f;
        float y = -(Math.floorDiv(index,GRID_WIDTH)) * 1.05f;
        RelativeHitbox.SetPercentageOffset(background_button.hb, x, y);
        RelativeHitbox.SetPercentageOffset(title_text.hb, x + baseTextOffset, y);
        RelativeHitbox.SetPercentageOffset(count_text.hb, x + baseCountOffset, y);

        return this;
    }

    public CardKeywordButton SetCardCount(ArrayList<AbstractCard> totalCards)
    {
        this.CardCount = totalCards != null ? JUtils.Count(totalCards, card -> card instanceof EYBCard && ((EYBCard) card).tooltips.contains(Tooltip)) : -1;
        count_text.SetText(CardCount);

        return this;
    }

    public CardKeywordButton SetOnClick(ActionT1<CardKeywordButton> onClick)
    {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void Update()
    {
        background_button.SetInteractable(GameEffects.IsEmpty()).Update();
        title_text.Update();
        count_text.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        title_text.Render(sb);
        if (CardCount >= 0) {
            count_text.Render(sb);
        }
        if (background_button.hb.hovered) {
            Tooltip.Render(sb, background_button.hb.x, background_button.hb.y, 0);
        }
    }
}

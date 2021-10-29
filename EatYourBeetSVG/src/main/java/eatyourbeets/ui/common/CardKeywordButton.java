package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
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

import static com.megacrit.cardcrawl.core.CardCrawlGame.popupMX;
import static com.megacrit.cardcrawl.core.CardCrawlGame.popupMY;

public class CardKeywordButton extends GUIElement
{
    private static final Color ACTIVE_COLOR = new Color(0.75f, 0.75f, 0.75f, 1f);
    private static final Color PANEL_COLOR = new Color(0.22f, 0.22f, 0.22f, 1f);
    private ActionT1<CardKeywordButton> onClick;

    public final EYBCardTooltip Tooltip;
    public final float baseCountOffset = -0.17f;
    public final float baseImageOffsetX = -0.27f;
    public final float baseImageOffsetY = 0.45f;
    public final float baseTextOffsetX = -0.10f;
    public final float baseTextOffsetY = 0f;
    public int CardCount = -1;

    public GUI_Button background_button;
    public GUI_Label tooltip_text;
    public GUI_Label title_text;
    public GUI_Label count_text;

    public CardKeywordButton(Hitbox hb, EYBCardTooltip tooltip)
    {
        final float iconSize = CardAffinityPanel.ICON_SIZE;

        Tooltip = tooltip;

        background_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 0.5f, 0).SetIsPopupCompatible(true))
        .SetColor(CardKeywordFilters.CurrentFilters.contains(Tooltip) ? ACTIVE_COLOR : PANEL_COLOR)
        .SetText("")
                .SetOnClick(button -> {
                    if (CardKeywordFilters.CurrentFilters.contains(Tooltip))
                    {
                        CardKeywordFilters.CurrentFilters.remove(Tooltip);
                        background_button.SetColor(PANEL_COLOR);
                        title_text.SetColor(Color.WHITE);
                    }
                    else
                    {
                        CardKeywordFilters.CurrentFilters.add(Tooltip);
                        background_button.SetColor(ACTIVE_COLOR);
                        title_text.SetColor(PANEL_COLOR);
                    }

                    if (this.onClick != null) {
                        this.onClick.Invoke(this);
                    }
                });

        tooltip_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
                new RelativeHitbox(hb, 1.2f, 1, baseImageOffsetX, baseImageOffsetY))
                .SetAlignment(0.5f, 0.5f, true) // 0.1f
                .SetText(Tooltip.icon != null ? "[" + Tooltip.id + "]" : "");

        title_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.5f, 1, baseTextOffsetX, baseTextOffsetY))
                .SetFont(EYBFontHelper.CardTooltipFont, 0.8f)
                .SetColor(CardKeywordFilters.CurrentFilters.contains(Tooltip) ? PANEL_COLOR : Color.WHITE)
        .SetAlignment(0.5f, 0.49f) // 0.1f
        .SetText(Tooltip.title);

        count_text = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
                new RelativeHitbox(hb, 0.28f, 1, baseCountOffset, 0f))
                .SetAlignment(0.5f, 0.51f) // 0.1f
                .SetColor(Settings.GOLD_COLOR)
                .SetText(CardCount);
    }

    public CardKeywordButton SetIndex(int index)
    {
        float x = (index % CardKeywordFilters.ROW_SIZE) * 1.06f;
        float y = -(Math.floorDiv(index,CardKeywordFilters.ROW_SIZE)) * 0.85f;
        RelativeHitbox.SetPercentageOffset(background_button.hb, x, y);
        RelativeHitbox.SetPercentageOffset(tooltip_text.hb, x + baseImageOffsetX, y + baseImageOffsetY);
        RelativeHitbox.SetPercentageOffset(title_text.hb, x + baseTextOffsetX, y + baseTextOffsetY);
        RelativeHitbox.SetPercentageOffset(count_text.hb, x + baseCountOffset, y);


        return this;
    }

    public CardKeywordButton SetCardCount(int count)
    {
        this.CardCount = count;
        count_text.SetText(count);

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
        tooltip_text.Update();
        title_text.Update();
        count_text.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);

        //if (Tooltip.icon != null) {
            //final float orbWidth = Tooltip.icon.getRegionWidth();
            //final float orbHeight = Tooltip.icon.getRegionHeight();
            //final float scaleX = 26.0F * Settings.scale / orbWidth;
            //final float scaleY = 26.0F * Settings.scale / orbHeight;
            //sb.setColor(Color.WHITE.cpy());
            //sb.draw(Tooltip.icon, background_button.hb.x + 30f * Settings.scale, background_button.hb.y, orbWidth / 2f, orbHeight / 2f, orbWidth, orbHeight, scaleX, scaleY, 0f);
        //}
        tooltip_text.Render(sb);
        title_text.Render(sb);
        if (CardCount >= 0) {
            count_text.Render(sb);
        }
        if (background_button.hb.hovered) {
            float actualMX;
            float actualMY;
            if (CardCrawlGame.isPopupOpen) {
                actualMX = popupMX;
                actualMY = popupMY;
            }
            else {
                actualMX = InputHelper.mX;
                actualMY = InputHelper.mY;
            }
            EYBCardTooltip.QueueTooltip(Tooltip, actualMX, actualMY);
        }
    }
}

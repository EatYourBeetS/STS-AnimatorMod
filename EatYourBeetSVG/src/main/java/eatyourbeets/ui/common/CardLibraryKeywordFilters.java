package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.InputManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class CardLibraryKeywordFilters extends GUIElement
{
    public static final int COLUMNS = 10;
    public static final float ROW_HEIGHT = ScreenH(0.04f);
    public boolean shouldRefresh = true;

    protected final HashMap<EYBCardTooltip, ArrayList<AbstractCard>> tooltipMap = new HashMap<>();
    protected final ArrayList<CardLibraryKeywordFilter> buttons = new ArrayList<>();
    protected final EYBCardTooltip tooltip;
    protected final GUI_Button toggle_button;
    protected final GUI_Image background_panel;
    protected boolean hasFocus;

    public CardLibraryKeywordFilters()
    {
        tooltip = new EYBCardTooltip("Toggle Filters", "You can also toggle card filters by #pRight-Clicking anywhere on the screen. Click the same filter twice to reset.");
        toggle_button = new GUI_Button(GR.Common.Images.Panel_Rounded.Texture(), new AdvancedHitbox(0, 0, ScreenW(0.1f), ScreenH(0.035f)))
        .SetFont(FontHelper.topPanelInfoFont, 0.85f)
        .SetColor(new Color(0.75f, 0.55f, 0.375f, 1f))
        .SetTextColor(Colors.White(1))
        .SetText("Filters")
        .SetPosition(ScreenW(0.91f), ScreenH(0.95f))
        .SetOnClick(this::ToggleFilters)
        .SetTooltip(tooltip, false);
        background_panel = new GUI_Image(GR.Common.Images.FullSquare.Texture(), new AdvancedHitbox(0, 0, ScreenW(0.7f), ScreenH(0.5f)))
        .SetPosition(ScreenW(0.5f), ScreenH(0.25f))
        .SetColor(0f, 0f, 0f, 0.8f);

        toggle_button.ShowTooltip(true);
        background_panel.SetActive(hasFocus = false);
    }

    @Override
    public void Update()
    {
        toggle_button.Update();

        if (background_panel.isActive)
        {
            for (CardLibraryKeywordFilter f : buttons)
            {
                f.Update();
            }

            background_panel.Update();
            hasFocus = background_panel.hb.hovered;
            background_panel.color.a = hasFocus ? 0.8f : 0.6f;
        }
        else
        {
            hasFocus = false;
        }

        if (InputManager.RightClick.IsJustReleased() && !CardCrawlGame.isPopupOpen)
        {
            ToggleFilters();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        toggle_button.Render(sb);

        if (background_panel.isActive)
        {
            background_panel.Render(sb);

            for (CardLibraryKeywordFilter f : buttons)
            {
                f.Render(sb);
            }
        }
    }

    public boolean HasFocus()
    {
        return hasFocus;
    }

    public void ToggleFocus()
    {
        hasFocus ^= true;
    }

    public void ToggleFilters()
    {
        SetFilters(!background_panel.isActive);
    }

    public void SetFilters(boolean setActive)
    {
        if (setActive)
        {
            if (shouldRefresh)
            {
                RefreshTooltips();
                shouldRefresh = false;
            }
        }
        else
        {
            hasFocus = false;
        }

        background_panel.SetActive(setActive);
    }

    private void RefreshTooltips()
    {
        tooltipMap.clear();
        buttons.clear();

        final LinkedHashSet<EYBCardTooltip> temp = new LinkedHashSet<>();
        final ArrayList<EYBCardTooltip> dynamicTooltips = new ArrayList<>();
        final ArrayList<AbstractCard> cards = CardLibrary.getAllCards();
        for (AnimatorCard_UltraRare card : AnimatorCard_UltraRare.GetCards().values())
        {
            if (AnimatorCard_UltraRare.IsSeen(card.cardID))
            {
                cards.add(card);
            }
        }
        for (AbstractCard c : cards)
        {
            if (c.isSeen && c instanceof EYBCard)
            {
                temp.clear();
                dynamicTooltips.clear();

                final EYBCard card = (EYBCard) c.makeCopy();
                temp.addAll(card.tooltips);
                card.GenerateDynamicTooltips(dynamicTooltips);
                card.upgrade();
                temp.addAll(card.tooltips);
                card.GenerateDynamicTooltips(dynamicTooltips);
                temp.addAll(dynamicTooltips);
                AddScalingTooltips(temp, card);

                for (EYBCardTooltip tooltip : temp)
                {
                    if (CardTooltips.FindByID(GR.Animator.PlayerClass, tooltip.id) != null)
                    {
                        tooltipMap.computeIfAbsent(tooltip, k -> new ArrayList<>()).add(c);
                    }
                }
            }
        }

        background_panel.hb.height = ROW_HEIGHT * (1f + (tooltipMap.size() / (float)COLUMNS));
        background_panel.SetPosition(ScreenW(0.5f), background_panel.hb.height / 2f);

        final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>(tooltipMap.keySet());
        final int maxRows = tooltips.size() / COLUMNS;
        tooltips.sort(Comparator.comparing(a -> a.title));
        int row = 0;
        int column = 0;
        for (EYBCardTooltip tooltip : tooltips)
        {
            AddKeywordButton(tooltip, row, column);

            if (++row > maxRows)
            {
                row = 0;
                column += 1;
            }
        }
    }

    private void AddKeywordButton(EYBCardTooltip tooltip, int row, int column)
    {
        final Hitbox parentHB = background_panel.hb;
        final int totalButtons = buttons.size();
        final float width = parentHB.width / COLUMNS;
        final float height = ROW_HEIGHT;
        final RelativeHitbox hb = new RelativeHitbox(parentHB, width, height, (width * (column + 0.5f)), parentHB.height - (height * (row + 0.5f)), false);
        buttons.add(new CardLibraryKeywordFilter(this, hb, row, column, tooltip));
    }

    private static void AddScalingTooltips(LinkedHashSet<EYBCardTooltip> tooltips, EYBCard card)
    {
        for (EYBCardAffinity a : card.affinities.List)
        {
            if (a.scaling > 0)
            {
                tooltips.add(a.type.GetScalingTooltip());
            }
        }
        if (card.affinities.Star != null && card.affinities.Star.scaling > 0)
        {
            tooltips.add(Affinity.Star.GetScalingTooltip());
        }
    }
}

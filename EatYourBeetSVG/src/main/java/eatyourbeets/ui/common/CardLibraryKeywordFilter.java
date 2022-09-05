package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;

import java.util.ArrayList;

public class CardLibraryKeywordFilter extends GUIElement implements FuncT1<Boolean, AbstractCard>
{
    protected GUI_Button button;
    protected EYBCardTooltip tooltip;
    protected CardLibraryKeywordFilters container;

    public CardLibraryKeywordFilter(CardLibraryKeywordFilters container, Hitbox hb, int row, int column, EYBCardTooltip tooltip)
    {
        final String text = ((tooltip.icon != null && CardTooltips.FindByID(GR.Animator.PlayerClass, tooltip.id) != null) ? ("[" + tooltip.id + "] ") : "") + tooltip.title;
        this.container = container;
        this.tooltip = tooltip;
        this.button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), hb)
        .SetBorder(GR.Common.Images.Panel_Rounded_Half_H_Border.Texture(), Colors.Gold(1))
        .SetFont(EYBFontHelper.CardTooltipFont, 1f)
        .SetColor(new Color(0.1f, 0.1f, 0.1f, 0.6f))
        .SetTextColor(Colors.Gold(1f))
        .SetNonInteractableTextColor(Colors.Gold(0.3f))
        .SetText(text, true)
        .SetOnClick(this::Filter);
        this.button.border.isActive = false;
        this.button.targetAlpha = this.button.currentAlpha = 0.05f;
    }

    @Override
    public void Update()
    {
        button.targetAlpha = container.hasFocus ? 1 : 0.05f;
        button.border.isActive = CustomCardLibSortHeader.Instance.filter == this;
        button.SetInteractable(container.hasFocus).Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        button.Render(sb);

        if (button.interactable && button.hb.hovered && !tooltip.title.startsWith("[")) // TODO: better logic for Affinity Scaling tooltips
        {
            final Boolean temp = tooltip.hideDescription;
            tooltip.hideDescription = false;
            tooltip.Render(sb, Settings.WIDTH * 0.01f, Settings.HEIGHT * 0.95f, 0);
            tooltip.hideDescription = temp;
        }
    }

    public void Filter()
    {
        CustomCardLibSortHeader.Instance.ApplyFilter(CustomCardLibSortHeader.Instance.filter == this ? null : this);
    }

    @Override
    public Boolean Invoke(AbstractCard card)
    {
        final ArrayList<AbstractCard> cards = container.tooltipMap.get(tooltip);
        return cards != null && cards.contains(card);
    }
}

package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.RenderHelpers;
import eatyourbeets.utilities.Testing;

public class BundledRelic
{
    private static final AnimatorStrings.Rewards text = GR.Animator.Strings.Rewards;
    public final AbstractCard card;
    public final ActionT1<BundledRelic> onSelect;
    public float textOffsetX;
    public float textOffsetY;
    public float iconOffsetX;
    public float iconOffsetY;
    public Color textColor;
    public String title;
    public Texture icon;
    public String tooltipHeader;
    public String tooltipBody;
    public Hitbox tooltipHB;
    public int amount;

    public BundledRelic(AbstractCard card, ActionT1<BundledRelic> onSelect)
    {
        this.card = card;
        this.onSelect = onSelect;
        this.tooltipHB = new Hitbox(0, 0, AbstractCard.RAW_W, AbstractCard.RAW_H);
    }

    public BundledRelic SetIcon(Texture icon, float iconOffsetX, float iconOffsetY)
    {
        this.icon = icon;
        this.iconOffsetX = iconOffsetX;
        this.iconOffsetY = iconOffsetY;

        return this;
    }

    public BundledRelic SetText(String text, Color textColor, float textOffsetX, float textOffsetY)
    {
        this.title = text;
        this.textColor = textColor;
        this.textOffsetX = textOffsetX;
        this.textOffsetY = textOffsetY;

        return this;
    }

    public BundledRelic SetTooltip(String header, String body)
    {
        this.tooltipHeader = header;
        this.tooltipBody = body;

        return this;
    }

    public BundledRelic SetAmount(int amount)
    {
        this.amount = amount;

        return this;
    }

    public void Open()
    {

    }

    public void Update()
    {
        if (tooltipBody != null)
        {
            tooltipHB.resize(card.drawScale * AbstractCard.IMG_WIDTH, (card.drawScale * AbstractCard.IMG_HEIGHT) * Testing.TryGetValue(0, 0.15f));
            tooltipHB.move(card.current_x, card.current_y + (textOffsetY * card.drawScale * Settings.scale));
            tooltipHB.update();

            if (tooltipHB.hovered)
            {
                TipHelper.renderGenericTip(tooltipHB.x + tooltipHB.width * 0.7f, tooltipHB.cY, tooltipHeader, tooltipBody);
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        BitmapFont font = FontHelper.buttonLabelFont;
        font.getData().setScale(card.drawScale * 0.8f);
        RenderHelpers.DrawOnCardAuto(sb, card, icon, iconOffsetX, iconOffsetY, icon.getWidth(), icon.getHeight());
        RenderHelpers.WriteOnCard(sb, card, font, title, textOffsetX, textOffsetY, textColor);
        RenderHelpers.ResetFont(font);
        tooltipHB.render(sb);
    }

    public void Acquired()
    {
        if (onSelect != null)
        {
            onSelect.Invoke(this);
        }
    }
}

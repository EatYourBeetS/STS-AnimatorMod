package pinacolada.cards.base.attributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.utilities.PCLRenderHelpers;

import java.util.HashMap;

public abstract class AbstractAttribute
{
    protected final static HashMap<AbstractCard.CardRarity, AdvancedTexture> panels = new HashMap<>();
    protected final static HashMap<AbstractCard.CardRarity, AdvancedTexture> panelsLarge = new HashMap<>();
    protected final static PCLImages.CardIcons ICONS = GR.PCL.Images.Icons;
    protected final static float DESC_OFFSET_X = (AbstractCard.IMG_WIDTH * 0.5f);
    protected final static float DESC_OFFSET_Y = (AbstractCard.IMG_HEIGHT * 0.10f);
    protected static final GlyphLayout layout = new GlyphLayout();

    public static boolean leftAlign;

    public Texture icon;
    public Texture largeIcon;
    public ColoredString mainText;
    public String iconTag;
    public String suffix;

    public abstract AbstractAttribute SetCard(PCLCard card);

    public AbstractAttribute SetIconTag(String iconTag)
    {
        this.iconTag = iconTag;

        return this;
    }

    public AbstractAttribute SetIcon(Texture icon)
    {
        this.icon = icon;

        return this;
    }

    public AbstractAttribute SetLargeIcon(Texture icon)
    {
        this.largeIcon = icon;

        return this;
    }

    public AbstractAttribute SetText(ColoredString string)
    {
        this.mainText = string;

        return this;
    }

    public AbstractAttribute SetText(Object text, Color color)
    {
        this.mainText = new ColoredString(text, color);

        return this;
    }

    public AbstractAttribute AddMultiplier(int times)
    {
        this.suffix = leftAlign ? ("x" + times) : (times + "x");

        return this;
    }

    public AbstractAttribute AddSuffix(String suffix)
    {
        this.suffix = suffix;

        return this;
    }

    public AbstractAttribute Clear()
    {
        this.suffix = null;
        this.iconTag = null;
        this.icon = null;
        this.largeIcon = null;
        this.mainText = null;

        return this;
    }

    public int GetParsedValue() {
        if (this.mainText == null) {
            return 0;
        }

        try {
            return Integer.parseInt(mainText.text);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public void Render(SpriteBatch sb, PCLCard card)
    {
        if (mainText == null) {
            return;
        }

        final float suffix_scale = 0.66f;
        final float cw = AbstractCard.RAW_W;
        final float ch = AbstractCard.RAW_H;
        final float b_w = 126f;
        final float b_h = 85f;
        final float y = -ch * 0.04f;
        final AdvancedTexture panel = GetPanelByRarity(card);

        BitmapFont largeFont = pinacolada.utilities.PCLRenderHelpers.GetLargeAttributeFont(card);
        largeFont.getData().setScale(card.isPopup ? 0.5f : 1);
        layout.setText(largeFont, mainText.text);

        float text_width = layout.width / Settings.scale;
        float suffix_width = 0;

        if (suffix != null)
        {
            layout.setText(largeFont, suffix);
            suffix_width = (layout.width / Settings.scale) * suffix_scale;
        }

        largeFont = PCLRenderHelpers.GetLargeAttributeFont(card);

        final float sign = leftAlign ? -1 : +1;
        final float icon_x = sign * (cw * 0.45f);
        float text_x = sign * cw * ((suffix != null || mainText.text.length() > 2) ? 0.375f : 0.35f);

        if (panel != null)
        {
            PCLRenderHelpers.DrawOnCardAuto(sb, card, panel.texture, new Vector2(sign * cw * 0.33f, y), b_w, b_h, panel.color, panel.color.a * card.transparency, 1, 0, leftAlign, false);
        }

        PCLRenderHelpers.DrawOnCardAuto(sb, card, card.isPopup ? largeIcon : icon, icon_x, y, 48, 48);
        PCLRenderHelpers.WriteOnCard(sb, card, largeFont, mainText.text, text_x - (sign * text_width * 0.5f), y, mainText.color, true);
        
        if (suffix != null)
        {
            largeFont.getData().setScale(largeFont.getScaleX() * suffix_scale);
            pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, largeFont, suffix, text_x - (sign * text_width) - (sign * suffix_width * 0.6f), y, mainText.color, true);
        }

        if (iconTag != null)
        {
            BitmapFont smallFont = PCLRenderHelpers.GetSmallAttributeFont(card);
            PCLRenderHelpers.WriteOnCard(sb, card, smallFont, iconTag, icon_x, y - 12, Settings.CREAM_COLOR, true);
            PCLRenderHelpers.ResetFont(smallFont);
        }

        PCLRenderHelpers.ResetFont(largeFont);
    }

    protected AdvancedTexture GetPanelByRarity(PCLCard card)
    {
        if (GR.PCL.Config.SimplifyCardUI.Get())
        {
            return null;
        }

        HashMap<AbstractCard.CardRarity, AdvancedTexture> map = card.isPopup ? panelsLarge : panels;
        AdvancedTexture result = map.getOrDefault(card.rarity, null);
        if (result == null)
        {
            result = new AdvancedTexture((card.isPopup ?
            GR.PCL.Images.CARD_BANNER_ATTRIBUTE_L: GR.PCL.Images.CARD_BANNER_ATTRIBUTE).Texture(),
            Color.WHITE.cpy().lerp(card.GetRarityColor(true), 0.25f));
            map.put(card.rarity, result);
        }

        return result;
    }
}
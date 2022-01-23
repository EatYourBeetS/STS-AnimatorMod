package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_TextBox;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLKeywordLegend extends GUIElement
{
    public GUI_Image image;
    public GUI_TextBox textBox;

    public PCLKeywordLegend(PCLCardTooltip tooltip)
    {
        image = PCLRenderHelpers.ForTexture(tooltip.icon.getTexture());
        textBox = new GUI_TextBox(GR.PCL.Images.Panel.Texture(), new Hitbox(0, 0, Scale(148), Scale(36)))
        .SetAlignment(0.5f, 0.31f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText(tooltip.title);
    }

    public PCLKeywordLegend SetPosition(float x, float y)
    {
        textBox.SetPosition(x, y);

        return this;
    }

    @Override
    public void Update()
    {
        textBox.Update();
        image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        textBox.Render(sb);
        float size = textBox.hb.width * 0.3f;
        image.Render(sb, textBox.hb.x /*+ (textBox.hb.width - size)*/, textBox.hb.cY - (size * 0.5f), size, size);
    }
}

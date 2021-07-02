package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

public class AlignmentCounter extends GUIElement
{
    public float iconSize = Scale(36);
    public GUI_Image image_alignmentWeak;
    public GUI_Image image_alignmentNormal;
    public GUI_TextBox textBox_counterWeak;
    public GUI_TextBox textBox_counterNormal;

    public AlignmentCounter(Texture icon)
    {
        image_alignmentWeak = RenderHelpers.ForTexture(GR.Common.Images.Alignments.Border_Weak.Texture()).SetBackgroundTexture(icon);
        image_alignmentNormal = RenderHelpers.ForTexture(GR.Common.Images.Alignments.Border.Texture()).SetBackgroundTexture(icon);

        textBox_counterWeak = new GUI_TextBox(GR.Common.Images.Panel.Texture(), new Hitbox(0, 0, Scale(96), Scale(36)))
        .SetAlignment(0.5f, 0.41f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText("TODO");

        textBox_counterNormal = new GUI_TextBox(GR.Common.Images.Panel.Texture(), new Hitbox(0, 0, Scale(96), Scale(36)))
        .SetAlignment(0.5f, 0.41f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText("TODO");
    }

    public AlignmentCounter SetPosition(float x, float y)
    {
        textBox_counterNormal.SetPosition(x, y);
        textBox_counterWeak.SetPosition(x + textBox_counterNormal.hb.width * 1.02f, y);

        return this;
    }

    @Override
    public void Update()
    {
        textBox_counterNormal.Update();
        textBox_counterWeak.Update();

        image_alignmentNormal.Update();
        image_alignmentWeak.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        textBox_counterWeak.Render(sb);
        textBox_counterNormal.Render(sb);

        image_alignmentWeak.Render(sb, textBox_counterWeak.hb.x /*- textBox_counterWeak.hb.width - iconSize*/,
                textBox_counterWeak.hb.cY - (iconSize * 0.5f), iconSize, iconSize);
        image_alignmentNormal.Render(sb, textBox_counterNormal.hb.x /*- textBox_counterNormal.hb.width - iconSize*/,
                textBox_counterNormal.hb.cY - (iconSize * 0.5f), iconSize, iconSize);
//        float size = textBox.hb.width * 0.3f;
//        image_Icon.Render(sb, textBox.hb.x /*+ (textBox.hb.width - size)*/, textBox.hb.cY - (size * 0.5f), size, size);
    }
}

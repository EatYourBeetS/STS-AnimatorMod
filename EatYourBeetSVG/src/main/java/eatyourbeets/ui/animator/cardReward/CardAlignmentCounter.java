package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardAlignment;
import eatyourbeets.cards.base.EYBCardAlignmentType;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

public class CardAlignmentCounter extends GUIElement
{
    public final EYBCardAlignmentType Type;
    public final EYBCardAlignment AlignmentLV1;
    public final EYBCardAlignment AlignmentLV2;
    public float Percentage;

    public float iconSize = Scale(44);
    public GUI_Image image_alignment;
    public GUI_TextBox textBox_counterWeak;
    public GUI_TextBox textBox_counterNormal;
    public GUI_TextBox textBox_counterPercentage;

    public CardAlignmentCounter(EYBCardAlignmentType alignment)
    {
        Type = alignment;
        AlignmentLV1 = new EYBCardAlignment(alignment, 0);
        AlignmentLV2 = new EYBCardAlignment(alignment, 0);

        image_alignment = RenderHelpers.ForTexture(alignment.GetIcon());

        textBox_counterWeak = new GUI_TextBox(GR.Common.Images.Panel.Texture(), new Hitbox(0, 0, Scale(72), Scale(36)))
        .SetAlignment(0.5f, 0.41f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText("4");

        textBox_counterNormal = new GUI_TextBox(GR.Common.Images.Panel.Texture(), new Hitbox(0, 0, Scale(72), Scale(36)))
        .SetAlignment(0.5f, 0.41f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText("5");

        textBox_counterPercentage = new GUI_TextBox(GR.Common.Images.Panel.Texture(), new Hitbox(0, 0, Scale(96), Scale(36)))
        .SetAlignment(0.5f, 0.41f) // 0.1f
        .SetColors(Color.DARK_GRAY, Settings.CREAM_COLOR)
        .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 1)
        .SetText("33%");
    }

    public CardAlignmentCounter SetPosition(float x, float y)
    {
        textBox_counterWeak.SetPosition(x, y);
        textBox_counterNormal.SetPosition(textBox_counterWeak.hb.x + textBox_counterWeak.hb.width * 1.15f, y);
        textBox_counterPercentage.SetPosition(textBox_counterNormal.hb.x + textBox_counterNormal.hb.width * 1.2f, y);

        return this;
    }

    @Override
    public void Update()
    {
        textBox_counterWeak.SetText(AlignmentLV1.level == 0 ? "-" : AlignmentLV1.level).Update();
        textBox_counterNormal.SetText(AlignmentLV2.level == 0 ? "-" : AlignmentLV2.level).Update();
        textBox_counterPercentage.SetText(Math.round(Percentage * 100) + "%").Update();

        image_alignment.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        textBox_counterPercentage.Render(sb);
        textBox_counterNormal.Render(sb);
        textBox_counterWeak.Render(sb);

        image_alignment.Render(sb, textBox_counterWeak.hb.x /*- textBox_counterWeak.hb.width*/ - iconSize,
                textBox_counterWeak.hb.cY - (iconSize * 0.5f), iconSize, iconSize);
    }
}

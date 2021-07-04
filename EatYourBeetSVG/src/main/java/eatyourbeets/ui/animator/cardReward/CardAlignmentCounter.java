package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardAlignment;
import eatyourbeets.cards.base.EYBCardAlignmentType;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

public class CardAlignmentCounter extends GUIElement
{
    public final EYBCardAlignmentType Type;
    public final EYBCardAlignment AlignmentLV1;
    public final EYBCardAlignment AlignmentLV2;
    public float Percentage;

    public GUI_Image image_panel;
    public GUI_Image image_alignment;
    public GUI_Label textBox_counterWeak;
    public GUI_Label textBox_counterNormal;
    public GUI_Label textBox_counterPercentage;

    public CardAlignmentCounter(Hitbox hb, EYBCardAlignmentType alignment)
    {
        final float iconSize = AnimatorCardRewardAlignments.ICON_SIZE;

        Type = alignment;
        AlignmentLV1 = new EYBCardAlignment(alignment, 0);
        AlignmentLV2 = new EYBCardAlignment(alignment, 0);

        image_panel = RenderHelpers.ForTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture())
        .SetHitbox(new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(0.05f, 0.05f, 0.05f, 1f);

        image_alignment = RenderHelpers.ForTexture(alignment.GetIcon())
        .SetHitbox(new RelativeHitbox(hb, iconSize, iconSize, -0.5f * (iconSize / hb.width), 0, true));

        textBox_counterWeak = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.15f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        textBox_counterNormal = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.45f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        textBox_counterPercentage = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
        new RelativeHitbox(hb, 0.38f, 1, 0.8f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("0%");
    }

    public CardAlignmentCounter SetIndex(int index)
    {
        float y = -index * 1.05f;
        SetY(image_panel.hb, y);
        SetY(textBox_counterNormal.hb, y);
        SetY(textBox_counterWeak.hb, y);
        SetY(textBox_counterPercentage.hb, y);
        SetY(image_alignment.hb, y);

        return this;
    }

    @Override
    public void Update()
    {
        image_panel.Update();
        textBox_counterWeak.SetText(AlignmentLV1.level == 0 ? "-" : AlignmentLV1.level).Update();
        textBox_counterNormal.SetText(AlignmentLV2.level == 0 ? "-" : AlignmentLV2.level).Update();
        textBox_counterPercentage.SetText(Math.round(Percentage * 100) + "%").Update();
        image_alignment.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_panel.Render(sb);
        textBox_counterPercentage.Render(sb);
        textBox_counterNormal.Render(sb);
        textBox_counterWeak.Render(sb);
        image_alignment.Render(sb);
    }

    protected void SetY(Hitbox hb, float y)
    {
        RelativeHitbox temp = (RelativeHitbox)hb;
        temp.SetPercentageOffset(temp.offset_cX, y);
    }
}

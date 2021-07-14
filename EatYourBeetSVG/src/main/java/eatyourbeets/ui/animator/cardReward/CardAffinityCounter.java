package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

public class CardAffinityCounter extends GUIElement
{
    public final AffinityType Type;
    public final EYBCardAffinity AffinityLV1;
    public final EYBCardAffinity AffinityLV2;
    public float Percentage;

    public GUI_Image image_panel;
    public GUI_Image image_affinity;
    public GUI_Label textBox_counterWeak;
    public GUI_Label textBox_counterNormal;
    public GUI_Label textBox_counterPercentage;

    public CardAffinityCounter(Hitbox hb, AffinityType alignment)
    {
        final float iconSize = AnimatorCardRewardAffinities.ICON_SIZE;

        Type = alignment;
        AffinityLV1 = new EYBCardAffinity(alignment, 0);
        AffinityLV2 = new EYBCardAffinity(alignment, 0);

        image_panel = RenderHelpers.ForTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture())
        .SetHitbox(new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(0.05f, 0.05f, 0.05f, 1f);

        image_affinity = RenderHelpers.ForTexture(alignment.GetIcon())
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

    public CardAffinityCounter SetIndex(int index)
    {
        float y = -index * 1.05f;
        SetY(image_panel.hb, y);
        SetY(textBox_counterNormal.hb, y);
        SetY(textBox_counterWeak.hb, y);
        SetY(textBox_counterPercentage.hb, y);
        SetY(image_affinity.hb, y);

        return this;
    }

    @Override
    public void Update()
    {
        image_panel.Update();
        textBox_counterWeak.SetText(AffinityLV1.level == 0 ? "-" : AffinityLV1.level).Update();
        textBox_counterNormal.SetText(AffinityLV2.level == 0 ? "-" : AffinityLV2.level).Update();
        textBox_counterPercentage.SetText(Math.round(Percentage * 100) + "%").Update();
        image_affinity.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image_panel.Render(sb);
        textBox_counterPercentage.Render(sb);
        textBox_counterNormal.Render(sb);
        textBox_counterWeak.Render(sb);
        image_affinity.Render(sb);
    }

    protected void SetY(Hitbox hb, float y)
    {
        RelativeHitbox temp = (RelativeHitbox)hb;
        temp.SetPercentageOffset(temp.offset_cX, y);
    }
}

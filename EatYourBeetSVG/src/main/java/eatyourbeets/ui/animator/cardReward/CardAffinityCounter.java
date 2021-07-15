package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardAffinityStatistics;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RenderHelpers;

public class CardAffinityCounter extends GUIElement
{
    private static final Color PANEL_COLOR = new Color(0.05f, 0.05f, 0.05f, 1f);
    private ActionT1<CardAffinityCounter> onClick;

    public final AffinityType Type;
    public EYBCardAffinityStatistics.Group AffinityGroup;

    public GUI_Button background_button;
    public GUI_Image affinity_image;
    public GUI_Label counterWeak_text;
    public GUI_Label counterNormal_text;
    public GUI_Label counterPercentage_text;

    public CardAffinityCounter(Hitbox hb, AffinityType type)
    {
        final float iconSize = CardAffinityPanel.ICON_SIZE;

        Type = type;

        background_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(PANEL_COLOR)
        .SetText("");

        affinity_image = RenderHelpers.ForTexture(type.GetIcon())
        .SetHitbox(new RelativeHitbox(hb, iconSize, iconSize, -0.5f * (iconSize / hb.width), 0, true));

        counterWeak_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.15f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        counterNormal_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.45f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        counterPercentage_text = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
        new RelativeHitbox(hb, 0.38f, 1, 0.8f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("0%");
    }

    public void Initialize(EYBCardAffinityStatistics statistics)
    {
        AffinityGroup = statistics.GetGroup(Type);
    }

    public CardAffinityCounter SetIndex(int index)
    {
        float y = -index * 1.05f;
        SetY(background_button.hb, y);
        SetY(counterNormal_text.hb, y);
        SetY(counterWeak_text.hb, y);
        SetY(counterPercentage_text.hb, y);
        SetY(affinity_image.hb, y);

        return this;
    }

    public CardAffinityCounter SetOnClick(ActionT1<CardAffinityCounter> onClick)
    {
        this.onClick = onClick;
        this.background_button.SetOnClick(() -> this.onClick.Invoke(this));

        return this;
    }

    @Override
    public void Update()
    {
        final int lv1 = AffinityGroup.GetTotal(1);
        final int lv2 = AffinityGroup.GetTotal(2);

        background_button.SetInteractable(GameEffects.IsEmpty()).Update();
        counterWeak_text.SetText(lv1 == 0 ? "-" : lv1).Update();
        counterNormal_text.SetText(lv2 == 0 ? "-" : lv2).Update();
        counterPercentage_text.SetText(AffinityGroup.GetPercentageString(0)).Update();
        affinity_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        counterPercentage_text.Render(sb);
        counterNormal_text.Render(sb);
        counterWeak_text.Render(sb);
        affinity_image.Render(sb);
    }

    protected void SetY(Hitbox hb, float y)
    {
        RelativeHitbox temp = (RelativeHitbox)hb;
        temp.SetPercentageOffset(temp.offset_cX, y);
    }
}

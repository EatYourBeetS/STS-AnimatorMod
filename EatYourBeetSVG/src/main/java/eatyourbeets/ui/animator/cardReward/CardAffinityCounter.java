package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
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

    public final Affinity Type;
    public EYBCardAffinityStatistics.Group AffinityGroup;

    public GUI_Button background_button;
    public GUI_Image affinity_image;
    public GUI_Label counter_text;
    public GUI_Label counterPercentage_text;

    public CardAffinityCounter(Hitbox hb, Affinity affinity)
    {
        final float iconSize = CardAffinityPanel.ICON_SIZE;

        Type = affinity;

        background_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(PANEL_COLOR)
        .SetText("");

        affinity_image = RenderHelpers.ForTexture(affinity.GetIcon())
        .SetHitbox(new RelativeHitbox(hb, iconSize, iconSize, -0.5f * (iconSize / hb.width), 0, true));

        counter_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.15f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        counterPercentage_text = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
        new RelativeHitbox(hb, 0.28f, 1, 0.45f, 0f))
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
        RelativeHitbox.SetPercentageOffset(background_button.hb, null, y);
        RelativeHitbox.SetPercentageOffset(counter_text.hb, null, y);
        RelativeHitbox.SetPercentageOffset(counterPercentage_text.hb, null, y);
        RelativeHitbox.SetPercentageOffset(affinity_image.hb, null, y);

        return this;
    }

    public CardAffinityCounter SetOnClick(ActionT1<CardAffinityCounter> onClick)
    {
        this.onClick = onClick;
        this.background_button.SetOnClick(onClick == null ? null : () -> this.onClick.Invoke(this));

        return this;
    }

    @Override
    public void Update()
    {
        final int lv1 = AffinityGroup.GetTotal(1);
        final int lv2 = AffinityGroup.GetTotal(2);

        background_button.SetInteractable(GameEffects.IsEmpty()).Update();
        counter_text.SetText(lv1 == 0 ? "-" : lv1).Update();
        counterPercentage_text.SetText(AffinityGroup.GetPercentageString(0)).Update();
        affinity_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        counterPercentage_text.Render(sb);
        counter_text.Render(sb);
        affinity_image.Render(sb);
    }
}

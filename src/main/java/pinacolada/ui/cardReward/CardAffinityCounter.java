package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinityStatistics;
import pinacolada.resources.GR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class CardAffinityCounter extends GUIElement
{
    private static final Color PANEL_COLOR = new Color(0.05f, 0.05f, 0.05f, 1f);
    private ActionT1<CardAffinityCounter> onClick;

    public final PCLAffinity Type;
    public PCLCardAffinityStatistics.Group AffinityGroup;

    public GUI_Button background_button;
    public GUI_Image affinity_image;
    public GUI_Label counterWeak_text;
    public GUI_Label counterPercentage_text;

    public CardAffinityCounter(Hitbox hb, PCLAffinity affinity)
    {
        final float iconSize = CardAffinityPanel.ICON_SIZE;

        Type = affinity;

        background_button = new GUI_Button(GR.PCL.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 0.5f, 0))
        .SetColor(PANEL_COLOR)
        .SetText("");

        affinity_image = PCLRenderHelpers.ForTexture(affinity.GetIcon())
        .SetHitbox(new RelativeHitbox(hb, iconSize, iconSize, -0.5f * (iconSize / hb.width), 0, true));

        counterWeak_text = new GUI_Label(EYBFontHelper.CardTooltipFont,
        new RelativeHitbox(hb, 0.28f, 1, 0.3f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("-");

        counterPercentage_text = new GUI_Label(EYBFontHelper.CardDescriptionFont_Normal,
        new RelativeHitbox(hb, 0.38f, 1, 0.8f, 0f))
        .SetAlignment(0.5f, 0.5f) // 0.1f
        .SetText("0%");
    }

    public void Initialize(PCLCardAffinityStatistics statistics)
    {
        AffinityGroup = statistics.GetGroup(Type);
    }

    public CardAffinityCounter SetIndex(int index)
    {
        float y = -index * 1.05f;
        RelativeHitbox.SetPercentageOffset(background_button.hb, null, y);
        RelativeHitbox.SetPercentageOffset(counterWeak_text.hb, null, y);
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

        background_button.SetInteractable(PCLGameEffects.IsEmpty()).Update();
        counterWeak_text.SetText(lv1 == 0 ? "-" : lv1).Update();
        counterPercentage_text.SetText(AffinityGroup.GetPercentageString(0)).Update();
        affinity_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        counterPercentage_text.Render(sb);
        counterWeak_text.Render(sb);
        affinity_image.Render(sb);
    }
}

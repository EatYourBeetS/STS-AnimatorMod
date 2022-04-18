package eatyourbeets.ui.animator.seriesSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorAscensionManager;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorAscensionEditor extends GUIElement
{
    private AnimatorAscensionManager ascensionManager;
    private final EYBCardTooltip tooltip;
    private final GUI_Label ascension_label;
    private final GUI_Label ascensionLevel_label;
    private final GUI_Button left_button;
    private final GUI_Button right_button;
    private final AdvancedHitbox hb;

    public AnimatorAscensionEditor(float cX, float cY)
    {
        hb = new AdvancedHitbox(Scale(120), Scale(80)).SetPosition(cX, cY);

        tooltip = new EYBCardTooltip("", "");

        ascension_label = new GUI_Label(FontHelper.tipBodyFont, new RelativeHitbox(hb, 1f, 0.5f, 0.5f, 0.75f))
        .SetAlignment(0.5f, 0.5f)
        .SetText(GR.Common.Strings.Ascension.Title);

        ascensionLevel_label = new GUI_Label(FontHelper.topPanelInfoFont, new RelativeHitbox(hb, 0.4f, 0.5f, 0.5f, 0.25f))
        .SetAlignment(0.5f, 0.5f);

        left_button = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, 0.3f, 0.5f, 0.15f, 0.25f))
        .SetText(null).SetOnClick(this::PreviousAscension);

        right_button = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, 0.3f, 0.5f, 0.85f, 0.25f))
        .SetText(null).SetOnClick(this::NextAscension);
    }

    public void Open()
    {
        ascensionManager = GameUtilities.GetAscensionData();
        RefreshAscension();
    }

    @Override
    public void Update()
    {
        hb.update();
        ascension_label.TryUpdate();
        ascensionLevel_label.TryUpdate();

        left_button.TryUpdate();
        right_button.TryUpdate();

        if (hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip, hb, false);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        ascension_label.TryRender(sb);
        ascensionLevel_label.TryRender(sb);
        left_button.TryRender(sb);
        right_button.TryRender(sb);
        hb.render(sb);
    }

    protected void PreviousAscension()
    {
        ascensionManager.Modifier = Math.max(0, ascensionManager.Modifier - 1);
        RefreshAscension();
    }

    protected void NextAscension()
    {
        ascensionManager.Modifier = Math.min(ascensionManager.MaxModifier, ascensionManager.Modifier + 1);
        RefreshAscension();
    }

    protected void RefreshAscension()
    {
        final int ascension = ascensionManager.GetAscensionLevel();
        final int actualAscension = ascensionManager.GetActualAscensionLevel();
        tooltip.title = GR.Common.Strings.Ascension.Title(actualAscension);
        tooltip.description = GR.Common.Strings.Ascension.GetDescription(actualAscension);
        ascensionLevel_label.SetText(actualAscension);
        left_button.SetActive(actualAscension > 20);
        right_button.SetActive(actualAscension >= 20 && (actualAscension < (20 + ascensionManager.MaxModifier)));
    }
}

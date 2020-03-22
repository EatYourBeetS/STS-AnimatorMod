package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorCardRewardInfo extends GUIElement
{
    public final GUI_Toggle upgradeToggle;
    public final GUI_Toggle zoomToggle;
    public final KeywordLegend exhaust;
    public final KeywordLegend ethereal;
    public final KeywordLegend retain;
    public final KeywordLegend innate;
    public final KeywordLegend purge;

    public AnimatorCardRewardInfo()
    {
        upgradeToggle = new GUI_Toggle(new Hitbox(Scale(256), Scale(48f)))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), ScreenH(0.65f))
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);

        zoomToggle = new GUI_Toggle(new Hitbox(Scale(256), Scale(48f)))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), ScreenH(0.65f) - (upgradeToggle.hb.height * 1.05f))
        .SetText(GR.Animator.Strings.Misc.DynamicPortraits)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetOnToggle(this::ToggleCardZoom);

        exhaust = new KeywordLegend(GR.Tooltips.Exhaust);
        ethereal = new KeywordLegend(GR.Tooltips.Ethereal);
        retain = new KeywordLegend(GR.Tooltips.Retain);
        innate = new KeywordLegend(GR.Tooltips.Innate);
        purge = new KeywordLegend(GR.Tooltips.Purge);
    }

    public void Close()
    {
        isActive = false;
        upgradeToggle.Toggle(false);
    }

    public void Open()
    {
        isActive = GameUtilities.IsPlayerClass(GR.Animator.PlayerClass);
        upgradeToggle.Toggle(false);
    }

    @Override
    public void Update()
    {
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
        zoomToggle.SetToggle(GR.Animator.Config.GetCropCardImages()).Update();

        float x = zoomToggle.hb.x + (zoomToggle.hb.width - (exhaust.textBox.hb.width * 0.5f));
        float step = exhaust.textBox.hb.height;
        float y = zoomToggle.hb.y - (step * 1.1f);
        exhaust.SetPosition(x, y).Update();
        ethereal.SetPosition(x, y - step).Update();
        retain.SetPosition(x, y - (step*2)).Update();
        innate.SetPosition(x, y - (step*3)).Update();
        purge.SetPosition(x, y - (step*4)).Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        upgradeToggle.Render(sb);
        zoomToggle.Render(sb);

        exhaust.Render(sb);
        ethereal.Render(sb);
        retain.Render(sb);
        innate.Render(sb);
        purge.Render(sb);
    }

    private void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    private void ToggleCardZoom(boolean value)
    {
        GR.Animator.Config.SetCropCardImages(value, true);
    }
}

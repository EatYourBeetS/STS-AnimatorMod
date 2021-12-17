package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.utilities.PCLGameUtilities;

public class PCLCardRewardInfo extends GUIElement
{
    public final GUI_Toggle upgradeToggle;
    public final GUI_Toggle zoomToggle;
    public final GUI_Toggle simplifyCardUIToggle;
    public final KeywordLegend exhaust;
    public final KeywordLegend ethereal;
    public final KeywordLegend retain;
    public final KeywordLegend innate;
    public final KeywordLegend purge;

    public PCLCardRewardInfo()
    {
        upgradeToggle = new GUI_Toggle(new Hitbox(Scale(256), Scale(48f)))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), ScreenH(0.65f))
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);

        zoomToggle = new GUI_Toggle(new Hitbox(Scale(256), Scale(48f)))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), upgradeToggle.hb.y - upgradeToggle.hb.height)
        .SetText(GR.PCL.Strings.Misc.DynamicPortraits)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetOnToggle(this::ToggleCardZoom);

        simplifyCardUIToggle = new GUI_Toggle(new Hitbox(Scale(256), Scale(48f)))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), zoomToggle.hb.y - zoomToggle.hb.height)
        .SetText(GR.PCL.Strings.Misc.SimplifyCardUI)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetOnToggle(this::ToggleSimplifyCardUI);

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
        isActive = PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass);
        upgradeToggle.Toggle(false);
    }

    @Override
    public void Update()
    {
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
        zoomToggle.SetToggle(GR.PCL.Config.CropCardImages.Get()).Update();
        simplifyCardUIToggle.SetToggle(GR.PCL.Config.SimplifyCardUI.Get()).Update();

        float x = zoomToggle.hb.x + (zoomToggle.hb.width - (exhaust.textBox.hb.width * 0.5f));
        float step = exhaust.textBox.hb.height;
        float y = simplifyCardUIToggle.hb.y - (step * 1.1f);
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
        simplifyCardUIToggle.Render(sb);

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
        GR.PCL.Config.CropCardImages.Set(value, true);
    }

    private void ToggleSimplifyCardUI(boolean value)
    {
        GR.PCL.Config.SimplifyCardUI.Set(value, true);
    }
}

package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorCardRewardInfo extends GUIElement
{
    public final GUI_Toggle upgradeToggle;

    public AnimatorCardRewardInfo()
    {
        upgradeToggle = new GUI_Toggle(new Hitbox(Scale(248), Scale(48.0F)))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(ScreenW(0.9f), ScreenH(0.65f))
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);
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
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        upgradeToggle.Render(sb);
    }

    private void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }
}

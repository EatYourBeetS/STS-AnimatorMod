package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RenderHelpers;

public class AffinityKeywordButton extends GUIElement
{
    public static final float ICON_SIZE = Scale(48);
    private static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    private ActionT1<AffinityKeywordButton> onClick;

    public final Affinity Type;

    public GUI_Button background_button;
    public final GUI_Image LV1_image;
    public final GUI_Image LV2_image;
    public int currentLevel;

    public AffinityKeywordButton(Hitbox hb, Affinity affinity, int initialAmount)
    {
        final float offset = -0.5f * (ICON_SIZE / hb.width);

        Type = affinity;
        currentLevel = initialAmount;

        background_button = new GUI_Button(affinity.GetIcon(), new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.5f, offset, true)
                .SetIsPopupCompatible(true))
                .SetText("")
                .SetColor(currentLevel == 0 ? PANEL_COLOR : Color.WHITE)
                .SetOnClick(() -> {
                    currentLevel = (currentLevel + 1) % 3;
                    background_button.SetColor(currentLevel == 0 ? PANEL_COLOR : Color.WHITE);

                    if (this.onClick != null) {
                        this.onClick.Invoke(this);
                    }
                });

        LV1_image = RenderHelpers.ForTexture(GR.Common.Images.Affinities.Border_Weak.Texture())
                .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.01f, offset, true));

        LV2_image = RenderHelpers.ForTexture(GR.Common.Images.Affinities.Border.Texture())
                .SetBackgroundTexture(GR.Common.Images.Affinities.BorderBG.Texture())
                .SetForegroundTexture(GR.Common.Images.Affinities.BorderFG.Texture())
                .SetHitbox(new RelativeHitbox(hb, ICON_SIZE, ICON_SIZE, 0.01f, offset, true));
    }

    public AffinityKeywordButton SetIndex(int index)
    {
        float x = 0.5f + (index * 1.05f);
        RelativeHitbox.SetPercentageOffset(background_button.hb, x, null);
        RelativeHitbox.SetPercentageOffset(LV1_image.hb, x, null);
        RelativeHitbox.SetPercentageOffset(LV2_image.hb, x, null);

        return this;
    }

    public AffinityKeywordButton SetOnClick(ActionT1<AffinityKeywordButton> onClick)
    {
        this.onClick = onClick;

        return this;
    }

    public void Reset(boolean invoke) {
        currentLevel = 0;
        background_button.SetColor(PANEL_COLOR);
        if (this.onClick != null && invoke) {
            this.onClick.Invoke(this);
        }
    }

    @Override
    public void Update()
    {
        background_button.SetInteractable(GameEffects.IsEmpty()).Update();
        LV1_image.Update();
        LV2_image.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        if (currentLevel == 1) {
            LV1_image.Render(sb);
        }
        else if (currentLevel == 2) {
            LV2_image.Render(sb);
        }
    }
}

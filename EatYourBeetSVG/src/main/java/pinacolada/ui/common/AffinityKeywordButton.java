package pinacolada.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.effects.affinity.ChangeAffinityCountEffect;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.ui.TextureCache;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class AffinityKeywordButton extends GUIElement
{
    public static final float ICON_SIZE = Scale(48);
    protected static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    protected static final TextureCache STAR_TEXTURE = GR.PCL.Images.Affinities.Star_FG;
    protected ActionT1<AffinityKeywordButton> onClick;
    protected Texture borderTexture;
    protected Texture borderBGTexture;
    protected Texture borderFGTexture;
    protected float radiusBG;

    public PCLAffinity Type;

    public GUI_Button background_button;
    public int currentLevel;
    public float borderRotation;

    public AffinityKeywordButton(Hitbox hb, PCLAffinity affinity) {
        this(hb, affinity, ICON_SIZE);
    }

    public AffinityKeywordButton(Hitbox hb, PCLAffinity affinity, float iconSize)
    {
        Type = affinity;

        background_button = new GUI_Button(affinity.GetIcon(), new RelativeHitbox(hb, iconSize, iconSize, 0f, 0f, true)
                .SetIsPopupCompatible(true))
                .SetText("")
                .SetColor(currentLevel == 0 ? PANEL_COLOR : Color.WHITE)
                .SetOnClick(() -> {
                    if (this.onClick != null) {
                        this.onClick.Invoke(this);
                    }
                });
        radiusBG = background_button.hb.width;

    }

    public AffinityKeywordButton SetAffinity(PCLAffinity affinity)
    {
        Type = affinity;
        background_button.SetBackground(affinity.GetIcon());

        return this;
    }

    public AffinityKeywordButton SetOffsets(float xOffset, float yOffset)
    {
        RelativeHitbox.SetPercentageOffset(background_button.hb, xOffset, yOffset);
        return this;
    }

    public AffinityKeywordButton SetOnClick(ActionT1<AffinityKeywordButton> onClick)
    {
        this.onClick = onClick;

        return this;
    }

    public AffinityKeywordButton SetLevel(int level) {
        currentLevel = level;
        background_button.SetColor(currentLevel == 0 ? PANEL_COLOR : Color.WHITE);
        switch (currentLevel) {
            case 1:
                borderTexture = GR.PCL.Images.Affinities.Border_Weak.Texture();
                borderBGTexture = null;
                borderFGTexture = null;
                radiusBG = background_button.hb.width;
                break;
            case 2:
                borderTexture = GR.PCL.Images.Affinities.Border_Strong.Texture();
                borderBGTexture = GR.PCL.Images.Affinities.BorderBG3.Texture();
                borderFGTexture = null;
                radiusBG = background_button.hb.width;
                break;
            case 3:
                borderTexture = GR.PCL.Images.Affinities.Border.Texture();
                borderBGTexture = GR.PCL.Images.Affinities.BorderBG2.Texture();
                borderFGTexture = GR.PCL.Images.Affinities.BorderFG.Texture();
                radiusBG = background_button.hb.width * 1.125f;
                break;
            default:
                borderTexture = null;
                borderBGTexture = null;
                borderFGTexture = null;
                radiusBG = background_button.hb.width;
        }
        return this;
    }

    public void Reset(boolean invoke) {
        currentLevel = 0;
        background_button.SetColor(PANEL_COLOR);
        if (this.onClick != null && invoke) {
            this.onClick.Invoke(this);
        }
    }

    public void Flash() {
        PCLGameEffects.List.Add(new ChangeAffinityCountEffect(this, true));
    }

    @Override
    public void Update()
    {
        background_button.SetInteractable(PCLGameEffects.IsEmpty()).Update();
        if (currentLevel > 2) {
            borderRotation = GR.UI.Time_Multi(-20);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        sb.setColor(background_button.buttonColor);
        if (borderBGTexture != null) {
            PCLRenderHelpers.DrawCentered(sb,
                    background_button.buttonColor,
                    borderBGTexture,
                    background_button.hb.cX,
                    background_button.hb.cY,
                    radiusBG,
                    radiusBG,
                    0.78f,
                    borderRotation);
        }
        background_button.Render(sb);
        sb.setColor(background_button.buttonColor);
        if (borderTexture != null) {
            sb.draw(borderTexture,
                    background_button.hb.x, background_button.hb.y,
                    background_button.hb.width/2f, background_button.hb.height/2f,
                    background_button.hb.width, background_button.hb.height,
                    background_button.background.scaleX, background_button.background.scaleY,
                    borderRotation, 0,0,
                    borderTexture.getWidth(), borderTexture.getHeight(), false, false
            );
        }
        if (borderFGTexture != null) {
            sb.draw(borderFGTexture,
                    background_button.hb.x, background_button.hb.y,
                    background_button.hb.width/2f, background_button.hb.height/2f,
                    background_button.hb.width, background_button.hb.height,
                    background_button.background.scaleX, background_button.background.scaleY,
                    -borderRotation, 0,0,
                    borderFGTexture.getWidth(), borderFGTexture.getHeight(), false, false
            );
        }
        if (Type  == PCLAffinity.Star) {
            Texture star = STAR_TEXTURE.Texture();
            sb.draw(star,
                    background_button.hb.x, background_button.hb.y,
                    background_button.hb.width/2f, background_button.hb.height/2f,
                    background_button.hb.width, background_button.hb.height,
                    background_button.background.scaleX, background_button.background.scaleY,
                    0, 0,0,
                    star.getWidth(), star.getHeight(), false, false
            );
        }
    }
}

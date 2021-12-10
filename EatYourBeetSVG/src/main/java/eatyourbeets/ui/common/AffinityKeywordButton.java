package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.affinity.ChangeAffinityCountEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.GameEffects;

public class AffinityKeywordButton extends GUIElement
{
    public static final float ICON_SIZE = Scale(48);
    protected static final Color PANEL_COLOR = new Color(0.3f, 0.3f, 0.3f, 1f);
    protected static final TextureCache STAR_TEXTURE = GR.Common.Images.Affinities.Star_FG;
    protected ActionT1<AffinityKeywordButton> onClick;
    protected Texture borderTexture;
    protected Texture borderBGTexture;
    protected Texture borderFGTexture;

    public Affinity Type;

    public GUI_Button background_button;
    public int currentLevel;
    public float borderRotation;

    public AffinityKeywordButton(Hitbox hb, Affinity affinity) {
        this(hb, affinity, ICON_SIZE);
    }

    public AffinityKeywordButton(Hitbox hb, Affinity affinity, float iconSize)
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
    }

    public AffinityKeywordButton SetAffinity(Affinity affinity)
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
                borderTexture = GR.Common.Images.Affinities.Border_Weak.Texture();
                borderBGTexture = null;
                borderFGTexture = null;
                break;
            case 2:
                borderTexture = GR.Common.Images.Affinities.Border_Weak.Texture();
                borderBGTexture = GR.Common.Images.Affinities.BorderBG.Texture();
                borderFGTexture = null;
                break;
            case 3:
                borderTexture = GR.Common.Images.Affinities.Border.Texture();
                borderBGTexture = GR.Common.Images.Affinities.BorderBG.Texture();
                borderFGTexture = GR.Common.Images.Affinities.BorderFG.Texture();
                break;
            default:
                borderTexture = null;
                borderBGTexture = null;
                borderFGTexture = null;
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
        GameEffects.List.Add(new ChangeAffinityCountEffect(this, true));
    }

    @Override
    public void Update()
    {
        background_button.SetInteractable(GameEffects.IsEmpty()).Update();
        if (currentLevel > 2) {
            borderRotation = GR.UI.Time_Multi(-20);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_button.Render(sb);
        //sb.setColor(Color.WHITE.cpy());
        if (borderBGTexture != null) {
            sb.draw(borderBGTexture,
                    background_button.hb.x, background_button.hb.y,
                    background_button.hb.width/2f, background_button.hb.height/2f,
                    background_button.hb.width, background_button.hb.height,
                    background_button.background.scaleX, background_button.background.scaleY,
                    0, 0,0,
                    borderBGTexture.getWidth(), borderBGTexture.getHeight(), false, false
                    );
        }
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
        if (Type  == Affinity.Star) {
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

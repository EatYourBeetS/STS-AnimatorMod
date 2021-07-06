package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class AnimatorTrophiesRenderer extends GUIElement
{
    protected static final AnimatorStrings.Trophies trophyStrings = GR.Animator.Strings.Trophies;
    protected static final AnimatorImages images = GR.Animator.Images;

    protected final Hitbox trophy1Hb;
    protected final Hitbox trophy2Hb;
    protected final Hitbox trophy3Hb;

    protected AnimatorLoadout loadout;
    protected AnimatorTrophies trophies;

    public AnimatorTrophiesRenderer()
    {
        trophy1Hb = new AdvancedHitbox(0, 0, 48 * Settings.scale, 48 * Settings.scale);
        trophy2Hb = new AdvancedHitbox(0, 0, 48 * Settings.scale, 48 * Settings.scale);
        trophy3Hb = new AdvancedHitbox(0, 0, 48 * Settings.scale, 48 * Settings.scale);

        float baseX = 200f * Settings.scale;
        float baseY = (float) Settings.HEIGHT / 2f;

        trophy1Hb.move(baseX + 380f * Settings.scale, baseY + 94f * Settings.scale);
        trophy2Hb.move(baseX + 440f * Settings.scale, baseY + 94f * Settings.scale);
        trophy3Hb.move(baseX + 500f * Settings.scale, baseY + 94f * Settings.scale);
    }

    public void Refresh(AnimatorLoadout loadout)
    {
        this.loadout = loadout;
        this.trophies = loadout.GetTrophies();
    }

    public void Update()
    {
        if (trophies == null)
        {
            return;
        }

        final float offsetX = 60 * Settings.scale;
        final float offsetY = 0;

        trophy1Hb.update();
        trophy2Hb.update();
        trophy3Hb.update();

        if (trophy1Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy1Hb.cX + offsetX, trophy1Hb.cY + offsetY, trophyStrings.Bronze, loadout.GetTrophyMessage(1));
        }
        else if (trophy2Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy2Hb.cX + offsetX, trophy2Hb.cY + offsetY, trophyStrings.Silver, loadout.GetTrophyMessage(2));
        }
        else if (trophy3Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy3Hb.cX + offsetX, trophy3Hb.cY + offsetY, trophyStrings.Gold, loadout.GetTrophyMessage(3));
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (trophies == null)
        {
            return;
        }

        FontHelper.tipHeaderFont.getData().setScale(0.6f);
        RenderTrophy(trophy1Hb, trophies.Trophy1, images.BRONZE_TROPHY.Texture(), sb);
        RenderTrophy(trophy2Hb, trophies.Trophy2, images.SILVER_TROPHY.Texture(), sb);
        RenderTrophy(trophy3Hb, trophies.Trophy3, images.GOLD_TROPHY.Texture(), sb);
        FontHelper.tipHeaderFont.getData().setScale(1);
    }

    private void RenderTrophy(Hitbox trophyHb, int trophyLevel, Texture texture, SpriteBatch sb)
    {
        Texture slotTexture;
        if (trophyLevel <= 0)
        {
            slotTexture = images.BRONZE_TROPHY_SLOT.Texture();
        }
        else
        {
            slotTexture = images.GOLD_TROPHY_SLOT.Texture();
        }

        final float w = 48;
        final float h = 48;
        final float halfW = 24;
        final float halfH = 24;

        sb.setColor(trophyHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(slotTexture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale,
                0f, 0, 0, 64, 64, false, false);

        Texture trophyTexture;
        if (trophyLevel < 0)
        {
            trophyTexture = images.LOCKED_TROPHY.Texture();
        }
        else
        {
            trophyTexture = texture;
        }

        sb.setColor(Color.WHITE);
        sb.draw(trophyTexture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale,
                0f, 0, 0, 64, 64, false, false);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyLevel + "/20",
                    trophyHb.cX + (5 * Settings.scale), trophyHb.y, Settings.GOLD_COLOR);
        }
    }
}
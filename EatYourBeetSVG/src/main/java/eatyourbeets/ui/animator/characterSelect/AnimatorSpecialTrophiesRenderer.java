package eatyourbeets.ui.animator.characterSelect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import eatyourbeets.dungeons.CustomAbstractDungeon;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;

public class AnimatorSpecialTrophiesRenderer
{
    protected static final AnimatorStrings.Trophies trophyStrings = GR.Animator.Text.Trophies;
    protected static final AnimatorImages.Textures textures = GR.Animator.Textures;

    protected final Hitbox trophySpecialHb;
    protected AnimatorTrophies specialTrophies;

    public AnimatorSpecialTrophiesRenderer()
    {
        trophySpecialHb = new Hitbox(64 * Settings.scale, 64 * Settings.scale);

        float baseX = 200.0F * Settings.scale;
        float baseY = (float)Settings.HEIGHT / 2.0F;

        trophySpecialHb.move(baseX + 492.0F * Settings.scale, baseY + 154.0F * Settings.scale);
    }

    public void Refresh()
    {
        this.specialTrophies = GR.Animator.Metrics.SpecialTrophies;
    }

    public void Update()
    {
        final float offsetX = 60 * Settings.scale;
        final float offsetY = 0;

        trophySpecialHb.update();

        if (trophySpecialHb.hovered)
        {
            String description = specialTrophies.Trophy1 > 0 ? trophyStrings.PlatinumDescription : trophyStrings.PlatinumHint;
            TipHelper.renderGenericTip(trophySpecialHb.cX + offsetX, trophySpecialHb.cY + offsetY, trophyStrings.Platinum, description);
        }
    }

    public void Render(SpriteBatch sb)
    {
        RenderSpecialTrophy(trophySpecialHb, specialTrophies.Trophy1, sb);
    }

    private static void RenderSpecialTrophy(Hitbox trophyHb, int trophyLevel, SpriteBatch sb)
    {
        String trophyString = "";
        if (trophyLevel > 0)
        {
            trophyString += " " + String.format("%.2f", CustomAbstractDungeon.GetUltraRareChance()) + "%";
        }

        float w = 64;
        float h = 64;
        float halfW = 32;
        float halfH = 32;

        sb.setColor(trophyHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(textures.PLATINUM_TROPHY_SLOT, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale,
                                    0.0F, 0, 0, 64, 64, false, false);

        Texture trophyTexture;
        if (trophyLevel <= 0)
        {
            trophyTexture = textures.LOCKED_TROPHY;
        }
        else
        {
            trophyTexture = textures.PLATINUM_TROPHY;
        }

        sb.setColor(Color.WHITE);
        sb.draw(trophyTexture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale,
                        0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyString,
            trophyHb.cX + (trophyHb.width * 1.3f * Settings.scale), trophyHb.cY, Settings.GOLD_COLOR);
        }
    }
}

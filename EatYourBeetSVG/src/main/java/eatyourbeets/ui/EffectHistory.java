package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.HashSet;

public class EffectHistory
{
    public static final HashSet<String> limitedEffects = new HashSet<>();
    public static final HashSet<String> semiLimitedEffects = new HashSet<>();

    private static final Hitbox HB_SemiLimited = new Hitbox(200 * Settings.scale, 80 * Settings.scale);
    private static final Hitbox HB_Limited = new Hitbox(200 * Settings.scale, 80 * Settings.scale);

    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final float h = 40 * Settings.scale;

    public static boolean HasActivatedLimited(String effectID)
    {
        return limitedEffects.contains(effectID);
    }

    public static boolean TryActivateLimited(String effectID)
    {
        return limitedEffects.add(effectID);
    }

    public static boolean HasActivatedSemiLimited(String effectID)
    {
        return semiLimitedEffects.contains(effectID);
    }

    public static boolean TryActivateSemiLimited(String effectID)
    {
        return semiLimitedEffects.add(effectID);
    }

    public static void Update()
    {

    }

    public static void Render(SpriteBatch sb)
    {
        // TODO: Show an icon near the player energy
    }

    private static void RenderBox(SpriteBatch sb, Hitbox hb)
    {
        float x = hb.x;
        float y = hb.cY;

        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
    }
}

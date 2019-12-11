package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.Resources_Common_Strings;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardBadge
{
    public static final Texture LegendTexture = new Texture("images/cardui/eyb/badges/_Legend.png");

    public static final EYBCardBadge Synergy = new EYBCardBadge(0, "images/cardui/eyb/badges/Synergy.png");
    public static final EYBCardBadge Discard = new EYBCardBadge(1, "images/cardui/eyb/badges/Discard.png");
    public static final EYBCardBadge Exhaust = new EYBCardBadge(2, "images/cardui/eyb/badges/Exhaust.png");
    public static final EYBCardBadge Drawn   = new EYBCardBadge(3, "images/cardui/eyb/badges/Drawn.png"  );
    public static final EYBCardBadge Special = new EYBCardBadge(4, "images/cardui/eyb/badges/Special.png");

    public final int id;
    public final Texture texture;
    public final String description;
    public final String name;

    private EYBCardBadge(int id, String texturePath)
    {
        this.name = Resources_Common_Strings.CardBadges.EXTRA_TEXT[id];
        this.description = Resources_Common_Strings.CardBadges.TEXT[id + 1];
        this.texture = AbstractResources.GetTexture(texturePath);
        this.id = id;
    }

    private static Vector2 dragStart = null;
    private static boolean dragging = false;
    private static Hitbox hitbox;

    public static void Open()
    {
        if (hitbox == null)
        {
            hitbox = new Hitbox(Settings.WIDTH * 0.85f, Settings.HEIGHT * 0.4f,
                    LegendTexture.getWidth() * Settings.scale,
                    LegendTexture.getHeight() * Settings.scale);
        }
    }

    public static void RenderLegend(SpriteBatch sb)
    {
        RenderHelpers.RenderOnScreen(sb, LegendTexture, hitbox.x, hitbox.y,
                hitbox.width / Settings.scale, hitbox.height / Settings.scale);

        float offset = hitbox.height * 0.2f;
        float y = hitbox.y + hitbox.height + (offset * 0.5f);

        RenderSingleBadge(sb, Synergy, y - (offset * 1));
        RenderSingleBadge(sb, Exhaust, y - (offset * 2));
        RenderSingleBadge(sb, Discard, y - (offset * 3));
        RenderSingleBadge(sb, Drawn  , y - (offset * 4));
        RenderSingleBadge(sb, Special, y - (offset * 5));

        if (Settings.isDebug)
        {
            hitbox.render(sb);
        }
    }

    private static void RenderSingleBadge(SpriteBatch sb, EYBCardBadge badge, float y)
    {
        RenderHelpers.RenderOnScreen(sb, badge.texture, hitbox.x, y - (hitbox.height * 0.09f), hitbox.height * 0.22f);
        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, badge.name, hitbox.x + (hitbox.width * 0.6f), y);
    }

    public static void UpdateLegend()
    {
        if (CardCrawlGame.isPopupOpen)
        {
            return;
        }

        hitbox.update();

        float mX = Gdx.input.getX();
        float mY = Settings.HEIGHT - Gdx.input.getY();

        if (hitbox.hovered || dragStart != null)
        {
            EYBCardText.ToggledOnce = true;

            if (InputHelper.justClickedLeft)
            {
                dragStart = new Vector2(mX, mY);
                return;
            }

            if (!InputHelper.justReleasedClickLeft && dragStart != null)
            {
                float max_X = Settings.WIDTH + (hitbox.width * 0.25f);
                float min_X = -hitbox.width * 0.25f;
                float max_Y = Settings.HEIGHT + (hitbox.height * 0.25f);
                float min_Y = -hitbox.height * 0.25f;

                float newX = Math.min(max_X, Math.max(min_X, hitbox.cX + (mX - dragStart.x)));
                float newY = Math.min(max_Y, Math.max(min_Y, hitbox.cY + (mY - dragStart.y)));

                hitbox.move(newX, newY);
                dragStart.set(mX, mY);
                return;
            }
        }

        dragStart = null;
    }

    public static void Close()
    {

    }
}

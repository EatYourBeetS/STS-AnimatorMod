package eatyourbeets.effects.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.Affinity;

public class FlashAffinityEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private final Texture img;
    private static final int W = 32;
    private float scale;

    public FlashAffinityEffect(Affinity affinity)
    {
        this.scale = Settings.scale;

        if (AbstractDungeon.player != null)
        {
            this.x = AbstractDungeon.player.hb.cX;
            this.y = AbstractDungeon.player.hb.cY;
        }

        this.img = affinity.GetIcon();
        this.duration = 0.7f;
        this.startingDuration = 0.7f;
        this.color = Color.WHITE.cpy();
        this.renderBehind = false;
    }

    public void update()
    {
        super.update();
        this.scale = Interpolation.exp5In.apply(Settings.scale, Settings.scale * 0.3f, this.duration / this.startingDuration);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        if (this.img != null && this.img.getWidth() >= 48)
        {
            sb.draw(img, x - 16f, y - 16f, 16f, 16f, 32f, 32f, scale * 12f, scale * 12f, 0f, 0, 0, 64, 64, false, false);
            sb.draw(img, x - 16f, y - 16f, 16f, 16f, 32f, 32f, scale * 10f, scale * 10f, 0f, 0, 0, 64, 64, false, false);
            sb.draw(img, x - 16f, y - 16f, 16f, 16f, 32f, 32f, scale * 8f, scale * 8f, 0f, 0, 0, 64, 64, false, false);
            sb.draw(img, x - 16f, y - 16f, 16f, 16f, 32f, 32f, scale * 7f, scale * 7f, 0f, 0, 0, 64, 64, false, false);
        }
        else
        {
            this.isDone = true;
        }
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {

    }
}


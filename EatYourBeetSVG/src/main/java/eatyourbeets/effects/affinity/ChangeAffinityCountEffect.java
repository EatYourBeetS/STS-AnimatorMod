package eatyourbeets.effects.affinity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.ui.animator.combat.EYBCardAffinityRow;

public class ChangeAffinityCountEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private static final float EFFECT_DUR = 2f;
    private float scale;
    private final Texture img;

    public ChangeAffinityCountEffect(EYBCardAffinityRow affinityRow, boolean playSfx)
    {
        this.img = affinityRow.Type.GetIcon();

        if (playSfx)
        {
            CardCrawlGame.sound.play("BUFF_1");
        }

        this.duration = 2f;
        this.startingDuration = 2f;
        this.scale = Settings.scale;
        this.color = new Color(1f, 1f, 1f, 0.5f);
        this.x = affinityRow.image_affinity.hb.x;
        this.y = affinityRow.image_affinity.hb.y;
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > 0.5f)
        {
            this.scale = Interpolation.exp5Out.apply(3f * Settings.scale, Settings.scale, -(this.duration - 2f) / 1.5f);
        }
        else
        {
            this.color.a = Interpolation.fade.apply(0.5f, 0f, 1f - this.duration);
        }

    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, x - 16f, y - 16f, 16f, 16f, 32f, 32f, scale, scale, 0f, 0, 0, 32, 32, false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {
    }
}


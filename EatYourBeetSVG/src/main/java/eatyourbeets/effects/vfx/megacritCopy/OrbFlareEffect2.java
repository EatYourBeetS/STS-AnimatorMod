package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import eatyourbeets.effects.EYBEffect;

public class OrbFlareEffect2 extends EYBEffect
{
    private static AtlasRegion outer;
    private static AtlasRegion inner;
    private float scaleY;
    private Color color2;
    private float cX;
    private float cY;

    public OrbFlareEffect2(float cX, float cY)
    {
        super(0.5f);

        if (outer == null)
        {
            outer = ImageMaster.vfxAtlas.findRegion("combat/orbFlareOuter");
            inner = ImageMaster.vfxAtlas.findRegion("combat/orbFlareInner");
        }

        this.cX = cX;
        this.cY = cY;
        this.renderBehind = true;
        this.scale = 2.0F * Settings.scale;
        this.scaleY = 0.0F;

        SetColors(OrbFlareEffect.OrbFlareColor.DARK);
    }

    public OrbFlareEffect2 SetColors(OrbFlareEffect.OrbFlareColor color)
    {
        switch (color)
        {
            case DARK:
                this.color = Color.VIOLET.cpy();
                this.color2 = Color.BLACK.cpy();
                break;
            case FROST:
                this.color = Settings.BLUE_TEXT_COLOR.cpy();
                this.color2 = Color.LIGHT_GRAY.cpy();
                break;
            case LIGHTNING:
                this.color = Color.CHARTREUSE.cpy();
                this.color2 = Color.WHITE.cpy();
                break;
            case PLASMA:
                this.color = Color.CORAL.cpy();
                this.color2 = Color.CYAN.cpy();
        }

        return this;
    }

    public OrbFlareEffect2 SetColors(Color color1, Color color2)
    {
        this.color.set(color1);
        this.color2.set(color2);

        return this;
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getRawDeltaTime();
        if (this.duration < 0.0F)
        {
            this.duration = 0.0F;
            this.isDone = true;
        }

        this.scaleY = Interpolation.elasticIn.apply(2.2F, 0.8F, this.duration * 2.0F);
        this.scale = Interpolation.elasticIn.apply(2.1F, 1.9F, this.duration * 2.0F);
        this.color.a = Interpolation.pow2Out.apply(0.0F, 0.6F, this.duration * 2.0F);
        this.color2.a = this.color.a;
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color2);
        sb.draw(inner, this.cX - (float) inner.packedWidth / 2.0F, this.cY - (float) inner.packedHeight / 2.0F, (float) inner.packedWidth / 2.0F, (float) inner.packedHeight / 2.0F, (float) inner.packedWidth, (float) inner.packedHeight, this.scale * Settings.scale * 1.1F, this.scaleY * Settings.scale, MathUtils.random(-1.0F, 1.0F));
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(outer, this.cX - (float) outer.packedWidth / 2.0F, this.cY - (float) outer.packedHeight / 2.0F, (float) outer.packedWidth / 2.0F, (float) outer.packedHeight / 2.0F, (float) outer.packedWidth, (float) outer.packedHeight, this.scale, this.scaleY * Settings.scale, MathUtils.random(-2.0F, 2.0F));
        sb.draw(outer, this.cX - (float) outer.packedWidth / 2.0F, this.cY - (float) outer.packedHeight / 2.0F, (float) outer.packedWidth / 2.0F, (float) outer.packedHeight / 2.0F, (float) outer.packedWidth, (float) outer.packedHeight, this.scale, this.scaleY * Settings.scale, MathUtils.random(-2.0F, 2.0F));
        sb.setBlendFunction(770, 771);
        sb.setColor(this.color2);
        sb.draw(inner, this.cX - (float) inner.packedWidth / 2.0F, this.cY - (float) inner.packedHeight / 2.0F, (float) inner.packedWidth / 2.0F, (float) inner.packedHeight / 2.0F, (float) inner.packedWidth, (float) inner.packedHeight, this.scale * Settings.scale * 1.1F, this.scaleY * Settings.scale, MathUtils.random(-1.0F, 1.0F));
    }

    public void dispose()
    {

    }
}

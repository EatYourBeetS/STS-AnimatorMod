package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.utilities.GameEffects;

public class GenericThrowEffect extends EYBEffectWithCallback<Hitbox>
{
    protected AbstractGameEffect hitEffect;
    protected Texture img;
    protected float x;
    protected float y;
    protected float startX;
    protected float startY;
    protected float targetX;
    protected float targetY;
    protected float targetXInitial;
    protected float targetYInitial;
    protected float vR;
    protected float sizeX;
    protected float sizeY;

    public GenericThrowEffect(Texture img, float startX, float startY, float targetX, float targetY)
    {
        super(0.5f);

        this.color = Color.WHITE.cpy();
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.x = startX;
        this.vR = 0;
        this.sizeX = img.getWidth();
        this.sizeY = img.getHeight();
        this.targetXInitial = targetX;
        this.targetYInitial = targetY;
        this.img = img;
    }

    public GenericThrowEffect SetColor(Color mainColor)
    {
        this.color = mainColor.cpy();

        return this;
    }

    public GenericThrowEffect SetHitEffect(AbstractGameEffect hitEffect)
    {
        this.hitEffect = hitEffect;
        return this;
    }

    public GenericThrowEffect SetImageParameters(float scale, float vR, float rotation)
    {
        this.scale = scale;
        this.vR = vR;
        this.rotation = rotation;

        return this;
    }

    public GenericThrowEffect SetSpread(float spreadX, float spreadY)
    {
        this.targetX = targetXInitial + MathUtils.random(-spreadX, spreadX) * Settings.scale;
        this.targetY = targetYInitial + MathUtils.random(-spreadY, spreadY) * Settings.scale;
        return this;
    }


    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x = Interpolation.linear.apply(targetX, startX, duration / startingDuration);
        y = Interpolation.linear.apply(targetY, startY, duration / startingDuration);
        rotation += vR * deltaTime;

        if (TickDuration(deltaTime))
        {
            if (hitEffect != null){
                GameEffects.Queue.Add(this.hitEffect);
            }
            Complete(new Hitbox(x, y, this.sizeX, this.sizeY));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.sizeX / 2.0F, this.sizeY / 2.0F, this.sizeX, this.sizeY, this.scale, this.scale, this.rotation, 0, 0, (int)this.sizeX, (int)this.sizeY, false, false);
    }
}

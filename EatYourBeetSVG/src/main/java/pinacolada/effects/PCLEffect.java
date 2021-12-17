package pinacolada.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public abstract class PCLEffect extends EYBEffect
{
    public static final PCLImages.Effects IMAGES = GR.PCL.Images.Effects;
    public static final Random RNG = new Random();
    public final AbstractPlayer player;
    public boolean isRealtime;
    public int ticks;

    public PCLEffect()
    {
        this(Settings.ACTION_DUR_FAST);
    }

    public PCLEffect(float duration)
    {
        this(duration, false);
    }

    public PCLEffect(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = this.startingDuration = duration;
        this.player = AbstractDungeon.player;
    }

    public PCLEffect SetRealtime(boolean isRealtime)
    {
        this.isRealtime = isRealtime;

        return this;
    }

    public PCLEffect SetRenderBehind(boolean renderBehind)
    {
        this.renderBehind = renderBehind;

        return this;
    }

    public PCLEffect SetDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = this.startingDuration = duration;

        return this;
    }

    public PCLEffect AddDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = (this.startingDuration += duration);

        return this;
    }

    public PCLEffect SetColor(Color color)
    {
        if (color != null)
        {
            if (this.color == null)
            {
                this.color = new Color(color.r, color.g, color.b, 1f);
            }
            else
            {
                this.color.r = color.r;
                this.color.g = color.g;
                this.color.b = color.b;
                // do not set alpha
            }
        }

        return this;
    }

    public PCLEffect SetRotation(float degrees)
    {
        this.rotation = degrees;

        return this;
    }

    @Override
    public void render(SpriteBatch sb)
    {

    }

    @Override
    public void update()
    {
        if (duration == startingDuration)
        {
            FirstUpdate();

            if (!this.isDone)
            {
                TickDuration(GetDeltaTime());
            }
        }
        else
        {
            UpdateInternal(GetDeltaTime());
        }
    }

    @Override
    public void dispose()
    {

    }

    protected void FirstUpdate()
    {

    }

    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete();
        }
    }

    protected void Complete()
    {
        PCLGameEffects.UnlistedEffects.remove(this);
        this.isDone = true;
    }

    protected boolean TickDuration(float deltaTime)
    {
        this.ticks += 1;
        this.duration -= deltaTime;

        if (this.duration < 0f && ticks >= 3) // ticks are necessary for SuperFastMode at 1000% speed
        {
            this.isDone = true;
        }

        return isDone;
    }

    protected float GetDeltaTime()
    {
        return isRealtime ? Gdx.graphics.getRawDeltaTime() : Gdx.graphics.getDeltaTime();
    }

    protected void RenderImage(SpriteBatch sb, TextureAtlas.AtlasRegion img, float x, float y)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, color, img, x, y, img.packedWidth, img.packedHeight, scale, rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected void RenderImage(SpriteBatch sb, Texture img, float x, float y, boolean flipX, boolean flipY)
    {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, color, img, x, y, img.getWidth(), img.getHeight(), scale, rotation, flipX, flipY);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected void RenderImage(SpriteBatch sb, Texture img, float x, float y, boolean flipX, boolean flipY, PCLRenderHelpers.BlendingMode blendingMode)
    {
        sb.setBlendFunction(blendingMode.srcFunc, blendingMode.dstFunc);
        PCLRenderHelpers.DrawCentered(sb, color, img, x, y, img.getWidth(), img.getHeight(), scale, rotation, flipX, flipY);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    protected static <T> T RandomElement(T[] source, RandomizedList<T> container)
    {
        if (container.Size() <= 1)
        {
            container.AddAll(source);
        }

        return container.RetrieveUnseeded(true);
    }

    protected static int Random(int min, int max)
    {
        return MathUtils.random(min, max);
    }

    protected static float Random(float min, float max)
    {
        return MathUtils.random(min, max);
    }

    protected static boolean RandomBoolean(float chance)
    {
        return MathUtils.randomBoolean(chance);
    }

    protected static boolean RandomBoolean()
    {
        return MathUtils.randomBoolean();
    }
}

package pinacolada.effects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.utilities.PCLRenderHelpers;

public abstract class PCLEffect extends EYBEffect
{
    public static final PCLImages.Effects IMAGES = GR.PCL.Images.Effects;

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
        super(duration, isRealtime);
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
}

package pinacolada.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLProjectile extends eatyourbeets.effects.Projectile
{
    public pinacolada.utilities.PCLRenderHelpers.BlendingMode blendingMode = pinacolada.utilities.PCLRenderHelpers.BlendingMode.Normal;

    public PCLProjectile(Texture texture, float width, float height)
    {
        super(texture, width, height);
    }

    public PCLProjectile SetBlendingMode(PCLRenderHelpers.BlendingMode blendingMode) {
        this.blendingMode = blendingMode;
        return this;
    }

    public void Render(SpriteBatch sb)
    {
        Render(sb, color == null ? sb.getColor() : color);
    }

    public void Render(SpriteBatch sb, Color color)
    {
        Render(sb, color, GetX(true), GetY(true), GetScale(true));
    }

    public void Render(SpriteBatch sb, Color color, float cX, float cY, float scale)
    {
        if (blendingMode != PCLRenderHelpers.BlendingMode.Normal) {
            sb.setBlendFunction(blendingMode.srcFunc, blendingMode.dstFunc);
            PCLRenderHelpers.DrawCentered(sb, color, texture, cX, cY, width, height, scale, GetRotation(true), flipX, flipY);
            sb.setBlendFunction(770, 771);
        }
        else {
            PCLRenderHelpers.DrawCentered(sb, color, texture, cX, cY, width, height, scale, GetRotation(true), flipX, flipY);
        }
    }
}

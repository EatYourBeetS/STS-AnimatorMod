package eatyourbeets.effects.stance;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class StanceAura extends EYBEffect
{
    protected static boolean switcher = true;
    protected TextureAtlas.AtlasRegion img;
    protected float x;
    protected float y;
    protected float vY;

    public StanceAura(Color color)
    {
        super(2f);

        this.img = ImageMaster.EXHAUST_L;
        this.scale = Random(2.7f, 2.5f);
        this.color = color.cpy();
        this.x = player.hb.cX + Random(-player.hb.width / 16f, player.hb.width / 16f);
        this.y = player.hb.cY + Random(-player.hb.height / 16f, player.hb.height / 12f);
//        this.x -= img.packedWidth * 0.5f;
//        this.y -= img.packedHeight * 0.5f;
        this.renderBehind = true;
        this.rotation = Random(0f, 360f);

        if (switcher ^= true)
        {
            this.renderBehind = true;
            this.vY = Random(0f, 40f);
        }
        else
        {
            this.renderBehind = false;
            this.vY = Random(0f, -40f);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (duration > 1f)
        {
            color.a = Interpolation.fade.apply(0.3f, 0f, this.duration - 1f);
        }
        else
        {
            color.a = Interpolation.fade.apply(0f, 0.3f, this.duration);
        }

        rotation += deltaTime * vY;

        super.UpdateInternal(deltaTime);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        RenderImage(sb, img, x, y);
    }
}

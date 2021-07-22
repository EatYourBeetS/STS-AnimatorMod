package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

import java.util.ArrayList;

public class RockBurstEffect extends EYBEffect
{
    private static final ArrayList<Texture> images = new ArrayList<>();
    private static final float DELAY = 0.05f;
    private static final int FRAMES = 5;
    private static final int SIZE = 192;

    private int frame;
    private float x;
    private float y;
    private float timer;

    public RockBurstEffect(float x, float y, float scale)
    {
        super(1f, true);

        this.x = x;
        this.y = y;
        this.frame = 0;
        this.scale = scale;
        this.color = Color.WHITE.cpy();
        this.timer = DELAY;

        if (images.isEmpty())
        {
            for (int i = 1; i <= FRAMES; i++)
            {
                images.add(ImageMaster.loadImage("images/effects/RockBurst" + i + ".png"));
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        this.timer -= deltaTime;

        if (this.timer < 0.0F)
        {
            this.timer = DELAY;

            if (frame == 0)
            {
                if (scale > 1)
                {
                    CardCrawlGame.sound.play("BLUNT_HEAVY");
                }
                else
                {
                    CardCrawlGame.sound.play("BLUNT_FAST");
                }
            }

            if ((frame + 1) < images.size())
            {
                this.frame += 1;
            }
            else
            {
                Complete();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        RenderImage(sb, images.get(frame), x, y);
    }
}

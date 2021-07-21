package eatyourbeets.effects.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class RockBurstEffect extends AbstractGameEffect
{
    private float x;
    private float y;
    private int frame = 0;
    private float animTimer = 0.03F;
    private static final ArrayList<Texture> images = new ArrayList<>();
    private static final int FRAME_END = 4;
    private static final int SIZE = 192;

    public RockBurstEffect(float x, float y, float scale) {
        this.x = x;
        this.y = y;
        this.frame = 0;
        this.scale = scale * Settings.scale;
        this.color = Color.WHITE.cpy();

        if (images.size() <= FRAME_END) {
            for (int i = 0; i <= FRAME_END; i++) {
                Texture img = ImageMaster.loadImage("images/orbs/animator/EarthEvoke" + i + ".png");
                images.add(img);
            }
        }
    }

    public void update() {
        this.animTimer -= Gdx.graphics.getDeltaTime();
        if (this.animTimer < 0.0F) {
            this.animTimer += 0.03F;
            ++this.frame;

            if (this.frame > FRAME_END) {
                this.frame = FRAME_END;
                this.isDone = true;
            }
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (frame < images.size()) {
            sb.draw(images.get(frame), this.x - (SIZE / 2f), this.y - (SIZE / 2f), SIZE / 2f, SIZE / 2f, SIZE, SIZE, this.scale, this.scale, this.rotation, 0, 0, SIZE, SIZE, false, false);
        }
    }

    public void dispose()
    {
    }
}

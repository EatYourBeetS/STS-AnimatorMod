package pinacolada.effects.affinity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import pinacolada.ui.combat.PCLAffinityRow;
import pinacolada.ui.common.AffinityKeywordButton;

public class ChangeAffinityCountEffect extends AbstractGameEffect
{
    private static final float EFFECT_DUR = 2f;
    private final float x;
    private final float y;
    private float scale;
    private int size = 32;
    private final Texture img;

    public ChangeAffinityCountEffect(AffinityKeywordButton button, boolean playSfx) {
        this(button.Type.GetIcon(), button.background_button.hb.x, button.background_button.hb.y, playSfx);
        size = 128;
    }

    public ChangeAffinityCountEffect(PCLAffinityRow affinityRow, boolean playSfx) {
        this(affinityRow.Type.GetIcon(), affinityRow.image_affinity.hb.x, affinityRow.image_affinity.hb.y, playSfx);
    }

    public ChangeAffinityCountEffect(Texture icon, float x, float y, boolean playSfx)
    {
        this.img = icon;

        if (playSfx)
        {
            CardCrawlGame.sound.play("BUFF_1");
        }

        this.duration = 1.5f;
        this.startingDuration = 1.5f;
        this.scale = Settings.scale;
        this.color = new Color(1f, 1f, 1f, 0.5f);
        this.x = x;
        this.y = y;
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
        sb.draw(this.img, x, y, size / 2f, size / 2f, size, size, scale, scale, 0f, 0, 0, size, size, false, false);
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {
    }
}


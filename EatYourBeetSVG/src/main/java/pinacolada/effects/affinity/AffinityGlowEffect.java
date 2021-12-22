package pinacolada.effects.affinity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Mathf;
import pinacolada.resources.GR;
import pinacolada.ui.common.AffinityKeywordButton;
import pinacolada.utilities.PCLRenderHelpers;

public class AffinityGlowEffect extends EYBEffect {

    public static final Color FALLBACK_COLOR = Color.valueOf("30c8dcff");
    protected AffinityKeywordButton source;
    protected Texture img;
    protected float scale;

    public AffinityGlowEffect(AffinityKeywordButton source) {
        this(source, FALLBACK_COLOR);
    }

    public AffinityGlowEffect(AffinityKeywordButton source, Color gColor) {
        this.duration = 1.2F;
        this.color = gColor != null ? gColor : FALLBACK_COLOR.cpy();
        this.img = GR.PCL.Images.Affinities.Border_Silhouette.Texture();
        this.source = source;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.scale = (0.54F + Interpolation.fade.apply(0.03F, 0.28F, 1F - this.duration)) * Settings.scale;
        this.color.a = Mathf.Clamp(this.duration - 0.4f, 0f, 1f);
        if (this.duration < 0.0F) {
            Complete();
            this.duration = 0.0F;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(PCLRenderHelpers.BlendingMode.Glowing.srcFunc, PCLRenderHelpers.BlendingMode.Glowing.dstFunc);
        PCLRenderHelpers.DrawCentered(sb, color, this.img, source.background_button.hb.cX, source.background_button.hb.cY, source.background_button.hb.width, source.background_button.hb.height, scale, 0, false, false);
        sb.setBlendFunction(PCLRenderHelpers.BlendingMode.Normal.srcFunc, PCLRenderHelpers.BlendingMode.Normal.dstFunc);
    }

    public void dispose() {
    }
}
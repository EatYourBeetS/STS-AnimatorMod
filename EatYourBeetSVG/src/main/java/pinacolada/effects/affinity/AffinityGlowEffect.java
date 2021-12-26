package pinacolada.effects.affinity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import pinacolada.resources.GR;
import pinacolada.ui.common.AffinityKeywordButton;
import pinacolada.utilities.PCLRenderHelpers;

public class AffinityGlowEffect extends EYBEffect {

    public static final Color FALLBACK_COLOR = Color.valueOf("30c8dcff");
    protected AffinityKeywordButton source;
    protected Texture img;
    protected float scale;

    public AffinityGlowEffect(AffinityKeywordButton source) {
        this(source, FALLBACK_COLOR.cpy());
    }

    public AffinityGlowEffect(AffinityKeywordButton source, Color gColor) {
        this.duration = 1.4F;
        this.color = gColor != null ? gColor : FALLBACK_COLOR.cpy();
        this.color.a = 0.45f;
        this.scale = 0.73f;
        this.img = GR.PCL.Images.Affinities.Border_Silhouette.Texture();
        this.source = source;
    }

    @Override
    protected void FirstUpdate() {
        this.color.a = 0.45f;
        this.scale = 0.73f;
    }

    public void update() {
        if (this.duration < 0.0F) {
            Complete();
            this.color.a = 0;
            this.scale = 0;
        }
        else {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.scale = (0.73F + Interpolation.fade.apply(0F, 0.37F, Math.max(0, 1.4F - this.duration))) * Settings.scale;
            this.color.a = Interpolation.fade.apply(0.5F, 0F, Math.max(0, 1.4F - this.duration));
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.isDone && this.duration >= 0.0F) {
            sb.setBlendFunction(PCLRenderHelpers.BlendingMode.Glowing.srcFunc, PCLRenderHelpers.BlendingMode.Glowing.dstFunc);
            PCLRenderHelpers.DrawCentered(sb, color, this.img, source.background_button.hb.cX, source.background_button.hb.cY, source.background_button.hb.width, source.background_button.hb.height, scale, 0, false, false);
            sb.setBlendFunction(PCLRenderHelpers.BlendingMode.Normal.srcFunc, PCLRenderHelpers.BlendingMode.Normal.dstFunc);
        }
    }

    public void dispose() {
    }
}
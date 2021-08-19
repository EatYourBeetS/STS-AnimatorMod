package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.orbs.FireOrbEvokeAction;
import eatyourbeets.actions.orbs.FireOrbPassiveAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.FadingParticleEffect;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Fire extends AnimatorOrb {
    public static final String ORB_ID = CreateFullID(Fire.class);
    private static final float RADIUS = 320;

    private static final TextureCache[] particles = {IMAGES.FireParticle1, IMAGES.FireParticle2, IMAGES.FireParticle3};
    private static final RandomizedList<TextureCache> textures = new RandomizedList<>();
    public static TextureCache imgExt = IMAGES.FireExternal;
    public static TextureCache imtInt = IMAGES.FireInternal;
    private final boolean hFlip1;
    private float vfxTimer = 0.4F;

    public static final int BURNING_AMOUNT = 1;

    public static Texture GetRandomTexture() {
        if (textures.Size() <= 1) // Adds some randomness but still ensures all textures are cycled through
        {
            textures.AddAll(particles);
        }

        return textures.RetrieveUnseeded(true).Texture();
    }

    public Fire() {
        super(ORB_ID, Timing.EndOfTurn);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = BURNING_AMOUNT * 3;
        this.basePassiveAmount = this.passiveAmount = 2;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    public void updateDescription() {
        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, BURNING_AMOUNT, this.evokeAmount);
    }

    @Override
    public void triggerEvokeAnimation() {
        super.triggerEvokeAnimation();

        SFX.Play(SFX.ATTACK_FIRE, 0.9f, 1.1f);
    }

    @Override
    public void applyFocus() {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
    }

    public void updateAnimation() {
        super.updateAnimation();

        this.angle += GR.UI.Delta(90f);
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            GameEffects.Queue.Add(new FadingParticleEffect(GetRandomTexture(), hb.cX + MathUtils.random(-32, 32), hb.cY - 32)
                    .SetTranslucent(1f)
                    .Edit(angle, (r, p) -> p
                            .SetFlip(MathUtils.randomBoolean(), false)
                            .SetScale(scale * MathUtils.random(0.08f, 0.32f))
                            .SetSpeed(0f, MathUtils.random(80f, 120f), 0f)
                            .SetAcceleration(0f, MathUtils.random(0f, 3f), null, null)
                            .SetTargetPosition(hb.cX, hb.cY + RADIUS)).SetDuration(1f, false));
            this.vfxTimer = MathUtils.random(0.1f, 0.4f);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        float scaleExt = this.bobEffect.y / 88f;
        float scaleInt = -(this.bobEffect.y / 100f);
        float angleExt = this.angle / 13f;
        float angleInt = -(this.angle / 8f);

        sb.draw(imgExt.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt, this.scale + scaleExt, angleExt, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imtInt.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleInt, this.scale + scaleInt, angleInt, 0, 0, 96, 96, this.hFlip1, false);

        this.renderText(sb);
        this.hb.render(sb);
    }

    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
    }

    @Override
    public void Evoke() {
        GameActions.Top.Add(new FireOrbEvokeAction(evokeAmount));

        super.Evoke();
    }

    @Override
    public void Passive() {
        GameActions.Bottom.Add(new FireOrbPassiveAction(this, passiveAmount));

        super.Passive();
    }

    @Override
    protected Color GetColor1() {
        return Color.FIREBRICK;
    }

    @Override
    protected Color GetColor2() {
        return Color.ORANGE;
    }
}
package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.actions.orbs.AirOrbEvokeAction;
import eatyourbeets.actions.orbs.AirOrbPassiveAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.FadingParticleEffect;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class Air extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Air.class);
    public static final int EVOKE_BASE_HITS = 4;
    public static final int EVOKE_DAMAGE_PER_HIT = 2;
    public static final int MAX_EVOKE_EFFECTS = 3;
    public static final int HAND_THRESHOLD = 5;
    private static final float RADIUS = 220;

    private float vfxTimer = 0.5F;
    public static TextureCache imgExt1 = IMAGES.AirLeft;
    public static TextureCache imgExt2 = IMAGES.AirRight;
    private final boolean hFlip1;

    public Air()
    {
        super(ORB_ID, Timing.EndOfTurn);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = EVOKE_BASE_HITS;
        this.basePassiveAmount = this.passiveAmount = 3;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription()
    {
        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, GetEvokeDamage(), this.evokeAmount);
    }

    @Override
    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play(SFX.POWER_FLIGHT, 0.2f);
        CardCrawlGame.sound.playAV(SFX.ORB_PLASMA_EVOKE, 1.2f, 0.7f);
    }

    @Override
    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
        this.evokeAmount = Math.max(0, this.baseEvokeAmount + GetFocus());

        CombatStats.OnOrbApplyFocus(this);
    }

    @Override
    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 180f;

        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            GameEffects.List.Add(new FadingParticleEffect(IMAGES.AirCloud.Texture(), hb.cX, hb.cY)
                    .SetColor(new Color(MathUtils.random(0.7f, 1f), 1, MathUtils.random(0.8f, 1f), MathUtils.random(0.5f, 0.8f)))
                    .SetBlendingMode(MathUtils.randomBoolean() ? RenderHelpers.BlendingMode.Overlay : RenderHelpers.BlendingMode.Normal)
                    .Edit(angle, (r, p) -> p
                            .SetScale(scale * MathUtils.random(0.1f, 0.32f)).SetTargetRotation(36000f,200f)
                            .SetSpeed(MathUtils.random(25f, 50f), MathUtils.random(25f, 50f), MathUtils.random(150f, 250f),MathUtils.random(0.3f, 0.6f))
                            .SetAcceleration(MathUtils.random(1f, 5f), MathUtils.random(1f, 5f), null, null, null)
                            .SetTargetPosition(hb.cX + RADIUS * MathUtils.cos(r), hb.cY + RADIUS * MathUtils.sin(r))).SetDuration(1f, false))
                    .SetRenderBehind(true);
            this.vfxTimer = MathUtils.random(0.1f, 0.25f);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float scaleExt1 = this.bobEffect.y / 77f;
        float scaleExt2 = this.bobEffect.y / 65f;
        float angleExt1 = this.angle * 1.3f;
        float angleExt2 = this.angle * 1.1f;

        sb.draw(imgExt1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, angleExt1, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imgExt2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt2, this.scale + scaleExt2, angleExt2, 0, 0, 96, 96, !this.hFlip1, false);

        sb.setBlendFunction(770, 1);
        this.shineColor.a = Interpolation.sine.apply(0.1f,0.33f, angleExt2 / 185);
        sb.setColor(this.shineColor);
        sb.draw(imgExt1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, this.angle * 1.8f, 0, 0, 96, 96, !this.hFlip1, false);

        this.shineColor.a = Interpolation.sine.apply(0.1f,0.33f, angleExt1 / 135);
        sb.setColor(this.shineColor);
        sb.draw(imgExt2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, this.angle * 1.2f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setBlendFunction(770, 771);

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb)
    {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET - 4f * Settings.scale, new Color(0.7f, 1f, 0.7f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, GetEvokeDamage() + "x" + this.evokeAmount, this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, new Color(0.2f, 0.9f, 0.2f, this.c.a), this.fontScale);
    }

    @Override
    public void playChannelSFX()
    {
        CardCrawlGame.sound.playAV(SFX.ATTACK_WHIRLWIND, 1.5f, 0.7f);
        CardCrawlGame.sound.playAV(SFX.ORB_PLASMA_CHANNEL, 1.2f, 0.7f);
        CardCrawlGame.sound.play(SFX.POWER_FLIGHT, 0.2f);
    }

    @Override
    public void Evoke()
    {
        super.Evoke();

        GameActions.Top.Add(new AirOrbEvokeAction(GetEvokeDamage(), this.evokeAmount));
        GameActions.Bottom.Cycle(name, 1);
    }

    @Override
    public void Passive()
    {
        super.Passive();

        GameActions.Bottom.Add(new AirOrbPassiveAction(this, this.passiveAmount));
    }

    @Override
    protected Color GetColor1()
    {
        return Color.FOREST;
    }

    @Override
    protected Color GetColor2()
    {
        return Color.CYAN;
    }

    private int GetEvokeDamage() {
        if (AbstractDungeon.player == null) {
            return EVOKE_DAMAGE_PER_HIT;
        }
        return EVOKE_DAMAGE_PER_HIT + AbstractDungeon.player.hand.size() / HAND_THRESHOLD;
    }
}
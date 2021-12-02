package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.orbs.WaterOrbEvokeAction;
import eatyourbeets.actions.orbs.WaterOrbPassiveAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.FadingParticleEffect;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public class Water extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Water.class);
    public static final int HP_HEAL = 2;
    private static final float RADIUS = 220;

    private final boolean hFlip1;
    private float vfxTimer = 0.5F;
    public int turns;
    public boolean evoked;

    public Water()
    {
        super(ORB_ID, Timing.EndOfTurn);

        this.hFlip1 = MathUtils.randomBoolean();
        this.evoked = false;
        this.baseEvokeAmount = this.evokeAmount = this.basePassiveAmount = this.passiveAmount = 2;


        this.updateDescription();
        this.channelAnimTimer = 0.5f;
        this.turns = 3;
    }

    @Override
    public void onChannel()
    {
        turns = 3;
        evoked = false;
        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    public void updateDescription()
    {
        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, this.evokeAmount, this.turns);
    }

    @Override
    public void triggerEvokeAnimation()
    {
        super.triggerEvokeAnimation();
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (!AbstractDungeon.player.orbs.contains(this))
        {
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            return;
        }

        this.turns -= 1;

        if (turns <= 0)
        {
            GameActions.Bottom.Add(new EvokeSpecificOrbAction(this));

            evoked = true;
        }
    }

    @Override
    public void onEndOfTurn()
    {
        if (!evoked && turns > 0)
        {
            Passive();
        }

        this.updateDescription();
    }

    @Override
    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());

        CombatStats.OnOrbApplyFocus(this);
    }

    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getRawDeltaTime() * 90f; //180f;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            GameEffects.Queue.Add(new FadingParticleEffect(IMAGES.WaterBubble.Texture(), hb.cX, hb.cY)
                    .SetBlendingMode(RenderHelpers.BlendingMode.Glowing)
                    .Edit(angle, (r, p) -> p
                            .SetScale(scale * MathUtils.random(0.08f, 0.32f)).SetTargetRotation(36000f,360f)
                            .SetSpeed(MathUtils.random(50f, 100f), MathUtils.random(50f, 100f), MathUtils.random(150f, 360f),0f)
                            .SetAcceleration(MathUtils.random(0f, 3f), MathUtils.random(0f, 3f), null, null, null)
                            .SetTargetPosition(hb.cX + RADIUS * MathUtils.cos(r), hb.cY + RADIUS * MathUtils.sin(r))).SetDuration(1f, false))
                    .SetRenderBehind(true);
            this.vfxTimer = MathUtils.random(0.2f, 0.5f);
        }
    }

    public void render(SpriteBatch sb)
    {
        float scaleExt = this.bobEffect.y / 84f;
        float scaleInt = - (this.bobEffect.y / 100f);
        float angleExt = this.angle * 3f;
        float angleExt2 = this.angle * 4f;
        float angleInt = - (this.angle / 2f);

        sb.setColor(this.c);
        sb.draw(IMAGES.Water1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleInt, this.scale + scaleInt, angleInt, 0, 0, 96, 96, this.hFlip1, false);
        sb.setBlendFunction(770, 1);

        this.shineColor.a = Interpolation.sine.apply(0.12f,0.62f, angleExt / 165);
        sb.setColor(this.shineColor);
        sb.draw(IMAGES.Water2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt, this.scale + scaleExt, angleExt, 0, 0, 96, 96, this.hFlip1, false);

        this.shineColor.a = Interpolation.sine.apply(0.05f,0.35f, angleExt / 135);
        sb.setColor(this.shineColor);
        sb.draw(IMAGES.Water3.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt, this.scale + scaleExt, angleExt * 2.5f, 0, 0, 96, 96, this.hFlip1, false);

        this.shineColor.a = Interpolation.sine.apply(0.12f,0.52f, angleExt2 / 185);
        sb.setColor(this.shineColor);
        sb.draw(IMAGES.Water4.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt, this.scale + scaleExt, angleExt2, 0, 0, 96, 96, this.hFlip1, false);
        sb.setBlendFunction(770, 771);

        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb)
    {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET - 4f * Settings.scale, new Color(0.2f, 1f, 1f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, new Color(0.2f, 0.5f, 0.9f, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turns), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2f + NUM_Y_OFFSET + 20f * Settings.scale, this.c, this.fontScale);
    }

    public void playChannelSFX()
    {
        SFX.Play(SFX.ANIMATOR_ORB_WATER_CHANNEL, 0.9f, 1.1f);
    }

    @Override
    public AbstractOrb makeCopy()
    {
        Water copy = new Water();
        if (!evoked)
        {
            copy.turns = turns;
        }

        return copy;
    }

    @Override
    public void Evoke()
    {
        GameActions.Top.Add(new WaterOrbEvokeAction(this.hb, evokeAmount));
        super.Evoke();
        turns = 0;
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        evoked = true;

        if (CombatStats.TryActivateLimited(ID))
        {
            GameActions.Bottom.Heal(HP_HEAL);
        }
    }

    @Override
    public void Passive()
    {
        GameActions.Bottom.Add(new WaterOrbPassiveAction(this, passiveAmount));

        super.Passive();
    }

    @Override
    protected Color GetColor1()
    {
        return Color.TEAL;
    }

    @Override
    protected Color GetColor2()
    {
        return Color.CYAN;
    }
}
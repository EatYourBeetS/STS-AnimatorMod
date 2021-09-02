package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.orbs.AirOrbEvokeAction;
import eatyourbeets.actions.orbs.AirOrbPassiveAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Air extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Air.class);
    public static final int EVOKE_BASE_HITS = 4;
    public static final int EVOKE_DAMAGE_PER_HIT = 2;
    public static final int MAX_EVOKE_EFFECTS = 3;

    public static TextureCache imgExt1 = IMAGES.AirLeft;
    public static TextureCache imgExt2 = IMAGES.AirRight;
    private final boolean hFlip1;

    public Air()
    {
        super(ORB_ID, Timing.EndOfTurn);

        this.hFlip1 = MathUtils.randomBoolean();
        this.baseEvokeAmount = this.evokeAmount = EVOKE_BASE_HITS;
        this.basePassiveAmount = this.passiveAmount = 4;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription()
    {
        this.applyFocus();
        this.description = JUtils.Format(orbStrings.DESCRIPTION[0], this.passiveAmount, this.evokeAmount);
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
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float scaleExt1 = this.bobEffect.y / 88f;
        float scaleExt2 = this.bobEffect.y / 77f;
        float angleExt1 = this.angle / 2.2f;
        float angleExt2 = this.angle / 1.5f;

        sb.draw(imgExt1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, angleExt1, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imgExt2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt2, this.scale + scaleExt2, angleExt2, 0, 0, 96, 96, this.hFlip1, false);

        sb.setBlendFunction(770, 1);
        this.shineColor.a = Interpolation.sine.apply(0.1f,0.33f, angleExt2 / 185);
        sb.setColor(this.shineColor);
        sb.draw(imgExt1.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, this.angle * 1.8f, 0, 0, 96, 96, this.hFlip1, false);

        this.shineColor.a = Interpolation.sine.apply(0.1f,0.33f, angleExt1 / 135);
        sb.setColor(this.shineColor);
        sb.draw(imgExt2.Texture(), this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt1, this.scale + scaleExt1, this.angle * 1.2f, 0, 0, 96, 96, this.hFlip1, false);
        sb.setBlendFunction(770, 771);

        this.renderText(sb);
        this.hb.render(sb);
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

        GameActions.Top.Add(new AirOrbEvokeAction(EVOKE_DAMAGE_PER_HIT, this.evokeAmount));
        if (CombatStats.TryActivateLimited(ID, MAX_EVOKE_EFFECTS))
        {
            GameActions.Bottom.Draw(1);
        }
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
}
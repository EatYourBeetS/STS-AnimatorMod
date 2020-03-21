package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import eatyourbeets.actions.orbs.FireOrbEvokeAction;
import eatyourbeets.actions.orbs.FireOrbPassiveAction;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Fire extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Fire.class);

    public static Texture imgExt;
    public static Texture imtInt;
    private final boolean hFlip1;

    public static final int BURNING_AMOUNT = 1;

    public Fire()
    {
        super(ORB_ID);

        if (imgExt == null)
        {
            imgExt = ImageMaster.loadImage("images/orbs/animator/FireExternal.png");
            imtInt = ImageMaster.loadImage("images/orbs/animator/FireInternal.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();

        this.baseEvokeAmount = this.evokeAmount = BURNING_AMOUNT * 3;
        this.basePassiveAmount = this.passiveAmount = 2;

        this.updateDescription();
        this.channelAnimTimer = 0.5f;
    }

    public void updateDescription()
    {
        String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = desc[0] + this.passiveAmount + desc[1] + BURNING_AMOUNT + desc[2] + this.evokeAmount + desc[3];
    }

    public void onEvoke()
    {
        GameActions.Top.Add(new FireOrbEvokeAction(evokeAmount));
    }

    public void onEndOfTurn()
    {
        GameActions.Bottom.Add(new FireOrbPassiveAction(passiveAmount));
    }

    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
        GameEffects.Queue.Add(new DarkOrbActivateEffect(this.cX, this.cY));
    }

    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
    }

    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 18f; //180f;
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);
        float scaleExt = this.bobEffect.y / 88f;
        float scaleInt = - (this.bobEffect.y / 100f);
        float angleExt = this.angle / 28f;
        float angleInt = - (this.angle / 10f);

        sb.draw(imgExt, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleExt, this.scale + scaleExt, angleExt, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imtInt, this.cX - 48f, this.cY - 48f, 48f, 48f, 96f, 96f, this.scale + scaleInt, this.scale + scaleInt, angleInt, 0, 0, 96, 96, this.hFlip1, false);

        this.renderText(sb);
        this.hb.render(sb);
    }

    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
    }
}
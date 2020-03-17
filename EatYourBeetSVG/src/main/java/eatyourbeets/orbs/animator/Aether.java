package eatyourbeets.orbs.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.actions.orbs.AetherOrbEvokeAction;
import eatyourbeets.actions.orbs.AetherOrbPassiveAction;
import eatyourbeets.orbs.AnimatorOrb;
import eatyourbeets.utilities.GameActions;

public class Aether extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Aether.class.getSimpleName());

    public static Texture imgExt1;
    public static Texture imgExt2;
    private final boolean hFlip1;

    public Aether()
    {
        super(ORB_ID);

        if (imgExt1 == null)
        {
            imgExt1 = ImageMaster.loadImage("images/orbs/animator/AirLeft.png");
            imgExt2 = ImageMaster.loadImage("images/orbs/animator/AirRight.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();

        this.baseEvokeAmount = this.evokeAmount = 3;
        this.basePassiveAmount = this.passiveAmount = 4;

        this.updateDescription();
        this.channelAnimTimer = 0.5F;
    }

    public void updateDescription()
    {
        String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = desc[0] + this.passiveAmount + desc[1] + this.evokeAmount + desc[2];
    }

    public void onEvoke()
    {
        GameActions.Top.Add(new AetherOrbEvokeAction(this.evokeAmount));
    }

    public void onEndOfTurn()
    {
        GameActions.Bottom.Add(new AetherOrbPassiveAction(this.passiveAmount));
    }

    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.2F);
        CardCrawlGame.sound.playAV("ORB_PLASMA_Evoke", 1.2f, 0.7f);
    }

    public void applyFocus()
    {
        this.passiveAmount = Math.max(0, this.basePassiveAmount + GetFocus());
    }

    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 180.0F;
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);

        float scaleExt1 = this.bobEffect.y / 88f;
        float scaleExt2 = this.bobEffect.y / 77f;
        float angleExt = this.angle / 12f;

        sb.draw(imgExt1, this.cX - 48.0F, this.cY - 48.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + scaleExt1, this.scale + scaleExt1, angleExt, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imgExt2, this.cX - 48.0F, this.cY - 48.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + scaleExt2, this.scale + scaleExt2, angleExt, 0, 0, 96, 96, this.hFlip1, false);

        this.renderText(sb);
        this.hb.render(sb);
    }

    public void playChannelSFX()
    {
        CardCrawlGame.sound.playAV("ATTACK_WHIRLWIND", 1.5f, 0.7f);
        CardCrawlGame.sound.playAV("ORB_PLASMA_CHANNEL", 1.2f, 0.7f);
        CardCrawlGame.sound.play("POWER_FLIGHT", 0.2F);
    }
}
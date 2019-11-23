package eatyourbeets.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.orbs.FireOrbEvokeAction;
import eatyourbeets.actions.orbs.FireOrbPassiveAction;
import eatyourbeets.utilities.GameUtilities;

public class Fire extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Fire.class.getSimpleName());

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
        this.channelAnimTimer = 0.5F;
    }

    public void updateDescription()
    {
        String[] desc = orbStrings.DESCRIPTION;

        this.applyFocus();
        this.description = desc[0] + this.passiveAmount + desc[1] + BURNING_AMOUNT + desc[2] + this.evokeAmount + desc[3];
    }

    public void onEvoke()
    {
        GameActionsHelper.AddToTop(new FireOrbEvokeAction(evokeAmount));
    }

    public void onEndOfTurn()
    {
        GameActionsHelper.AddToBottom(new FireOrbPassiveAction(this));
    }

    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1F);
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
    }

    public void applyFocus()
    {
        int focus = GameUtilities.GetFocus(AbstractDungeon.player);

        this.passiveAmount = Math.max(0, this.basePassiveAmount + focus);
    }

    public void updateAnimation()
    {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 18f; //180.0F;
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.c);
        float scaleExt = this.bobEffect.y / 88f;
        float scaleInt = - (this.bobEffect.y / 100f);
        float angleExt = this.angle / 28f;
        float angleInt = - (this.angle / 10f);

        sb.draw(imgExt, this.cX - 48.0F, this.cY - 48.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + scaleExt, this.scale + scaleExt, angleExt, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(imtInt, this.cX - 48.0F, this.cY - 48.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + scaleInt, this.scale + scaleInt, angleInt, 0, 0, 96, 96, this.hFlip1, false);

        this.renderText(sb);
        this.hb.render(sb);
    }

    public void playChannelSFX()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
    }
}
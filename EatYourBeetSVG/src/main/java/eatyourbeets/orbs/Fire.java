package eatyourbeets.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.FireOrbEvokeAction;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class Fire extends AnimatorOrb
{
    public static final String ORB_ID = CreateFullID(Fire.class.getSimpleName());

    public static Texture imgExt;
    public static Texture imtInt;
    private final boolean hFlip1;

    private boolean evoked;

    public Fire()
    {
        super(ORB_ID);

        if (imgExt == null)
        {
            imgExt = ImageMaster.loadImage("images/orbs/" + ORB_ID + "External.png");
            imtInt = ImageMaster.loadImage("images/orbs/" + ORB_ID + "Internal.png");
        }

        this.hFlip1 = MathUtils.randomBoolean();
        this.evoked = false;
        this.baseEvokeAmount = 4;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 2;
        this.passiveAmount = this.basePassiveAmount;
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
        if (evokeAmount > 0)
        {
            GameActionsHelper.AddToBottom(new FireOrbEvokeAction(evokeAmount));
        }

        evoked = true;
    }

    public void onEndOfTurn()
    {
        if (evoked)
        {
            return;
        }

        int maxHealth = Integer.MIN_VALUE;
        AbstractMonster enemy = null;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.currentHealth > maxHealth)
            {
                maxHealth = m.currentHealth;
                enemy = m;
            }
        }

        if (enemy != null)
        {
            GameActionsHelper.ApplyPower(p, enemy, new BurningPower(enemy, p, this.passiveAmount), this.passiveAmount);
        }

        this.updateDescription();
    }

    public void triggerEvokeAnimation()
    {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1F);
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
    }

    public void applyFocus()
    {
//        AbstractPower power = AbstractDungeon.player.getPower("Focus");
//        if (power != null)
//        {
//            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
//            this.evokeAmount = Math.max(0, this.baseEvokeAmount + power.amount);
//        }
//        else
//        {
//            this.evokeAmount = this.baseEvokeAmount;
//            this.passiveAmount = this.basePassiveAmount;
//        }
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
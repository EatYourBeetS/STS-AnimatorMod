package eatyourbeets.effects.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashAttackEffect extends AbstractGameEffect
{
    private static int blockSound = 0;
    public TextureAtlas.AtlasRegion img;
    private float x;
    private float y;
    private float sY;
    private float tY;
    private static final float DURATION = 0.6F;
    private AbstractGameAction.AttackEffect effect;
    private boolean triggered;

    public FlashAttackEffect(float x, float y, AbstractGameAction.AttackEffect effect, boolean mute)
    {
        this.triggered = false;
        this.duration = 0.6F;
        this.startingDuration = 0.6F;
        this.effect = effect;
        this.img = this.loadImage();

        if (this.img != null)
        {
            this.x = x - (float) this.img.packedWidth / 2.0F;
            y -= (float) this.img.packedHeight / 2.0F;
        }

        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        if (!mute)
        {
            this.playSound(effect);
        }

        if (effect == AbstractGameAction.AttackEffect.SHIELD)
        {
            this.y = y + 80.0F * Settings.scale;
            this.sY = this.y;
            this.tY = y;
        }
        else
        {
            this.y = y;
            this.sY = y;
            this.tY = y;
        }
    }

    public FlashAttackEffect(float x, float y, AbstractGameAction.AttackEffect effect)
    {
        this(x, y, effect, false);
    }

    private TextureAtlas.AtlasRegion loadImage()
    {
        switch (this.effect)
        {
            case SHIELD:
                return ImageMaster.ATK_SHIELD;
            case SLASH_DIAGONAL:
                return ImageMaster.ATK_SLASH_D;
            case SLASH_HEAVY:
                return ImageMaster.ATK_SLASH_HEAVY;
            case SLASH_HORIZONTAL:
                return ImageMaster.ATK_SLASH_H;
            case SLASH_VERTICAL:
                return ImageMaster.ATK_SLASH_V;
            case BLUNT_LIGHT:
                return ImageMaster.ATK_BLUNT_LIGHT;
            case BLUNT_HEAVY:
                this.rotation = MathUtils.random(360.0F);
                return ImageMaster.ATK_BLUNT_HEAVY;
            case FIRE:
                return ImageMaster.ATK_FIRE;
            case POISON:
                return ImageMaster.ATK_POISON;
            case NONE:
                return null;
            default:
                return ImageMaster.ATK_SLASH_D;
        }
    }

    private void playSound(AbstractGameAction.AttackEffect effect)
    {
        switch (effect)
        {
            case SHIELD:
                this.playBlockSound();
                break;
            case SLASH_DIAGONAL:
            case SLASH_HORIZONTAL:
            case SLASH_VERTICAL:
            default:
                CardCrawlGame.sound.play("ATTACK_FAST");
                break;
            case SLASH_HEAVY:
                CardCrawlGame.sound.play("ATTACK_HEAVY");
                break;
            case BLUNT_LIGHT:
                CardCrawlGame.sound.play("BLUNT_FAST");
                break;
            case BLUNT_HEAVY:
                CardCrawlGame.sound.play("BLUNT_HEAVY");
                break;
            case FIRE:
                CardCrawlGame.sound.play("ATTACK_FIRE");
                break;
            case POISON:
                CardCrawlGame.sound.play("ATTACK_POISON");
            case NONE:
        }

    }

    private void playBlockSound()
    {
        if (blockSound == 0)
        {
            CardCrawlGame.sound.play("BLOCK_GAIN_1");
        }
        else if (blockSound == 1)
        {
            CardCrawlGame.sound.play("BLOCK_GAIN_2");
        }
        else
        {
            CardCrawlGame.sound.play("BLOCK_GAIN_3");
        }

        ++blockSound;
        if (blockSound > 2)
        {
            blockSound = 0;
        }
    }

    public void update()
    {
        if (this.effect == AbstractGameAction.AttackEffect.SHIELD)
        {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F)
            {
                this.isDone = true;
                this.color.a = 0.0F;
            }
            else if (this.duration < 0.2F)
            {
                this.color.a = this.duration * 5.0F;
            }
            else
            {
                this.color.a = Interpolation.fade.apply(1.0F, 0.0F, this.duration * 0.75F / 0.6F);
            }

            this.y = Interpolation.exp10In.apply(this.tY, this.sY, this.duration / 0.6F);
            if (this.duration < 0.4F && !this.triggered)
            {
                this.triggered = true;
            }
        }
        else
        {
            super.update();
        }

    }

    public void render(SpriteBatch sb)
    {
        if (this.img != null)
        {
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float) this.img.packedWidth / 2.0F, (float) this.img.packedHeight / 2.0F, (float) this.img.packedWidth, (float) this.img.packedHeight, this.scale, this.scale, this.rotation);
        }
    }

    public void dispose()
    {
    }
}

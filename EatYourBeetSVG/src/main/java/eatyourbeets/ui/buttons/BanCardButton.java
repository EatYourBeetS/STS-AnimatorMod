package eatyourbeets.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.resources.GR;

public class BanCardButton
{
    private static final int W = 512;
    private static final int H = 256;

    private float showTimer;
    private float current_x;
    private float target_x;
    private boolean isHidden;
    private Color textColor;
    private Color btnColor;
    private static final float HITBOX_W;
    private static final float HITBOX_H;
    public Hitbox hb;
    public boolean banned;

    private final float HIDE_X;
    private float SHOW_X;
    private float SHOW_Y;
    public AbstractCard card;

    public BanCardButton(AbstractCard card)
    {
        this.showTimer = -1;
        this.card = card;
        this.SHOW_X = card.target_x;
        this.SHOW_Y = card.target_y + (Settings.scale * 200);
        this.HIDE_X = (float) Settings.WIDTH / 2.0F;
        this.current_x = HIDE_X;
        this.target_x = this.current_x;
        this.isHidden = true;
        this.textColor = Color.WHITE.cpy();
        this.btnColor = Color.RED.cpy();
        this.hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);
        this.hb.move(SHOW_X, SHOW_Y);
    }

    public void update()
    {
        updatePosition();

        if (showTimer >= 0)
        {
            showTimer -= Gdx.graphics.getDeltaTime();
            if (showTimer < 0)
            {
                this.isHidden = false;
            }
        }
        else if (!this.isHidden && this.card.targetDrawScale < 1)
        {
            this.hb.update();
//
//            if (this.current_x != this.target_x)
//            {
//                this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
//                if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD)
//                {
//                    this.current_x = this.target_x;
//                    this.hb.move(this.current_x, SHOW_Y);
//                }
//            }

            if (this.hb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.hb.hovered && InputHelper.justClickedLeft)
            {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (this.hb.clicked)// || InputActionSet.cancel.isJustPressed() || CInputActionSet.cancel.isJustPressed())
            {
                this.hb.clicked = false;
                this.banned = true;
            }

            this.textColor.a = MathHelper.fadeLerpSnap(this.textColor.a, 1.0F);
            this.btnColor.a = this.textColor.a;
        }
    }

    protected void updatePosition()
    {
        this.target_x = this.SHOW_X = card.target_x;
        this.SHOW_Y = card.target_y + (Settings.scale * 200);

        if (this.current_x != this.target_x)
        {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD)
            {
                this.current_x = this.target_x;
                this.hb.move(this.current_x, SHOW_Y);
            }
        }
        else
        {
            this.hb.move(SHOW_X, SHOW_Y);
        }
    }

    public void hideInstantly()
    {
        this.current_x = HIDE_X;
        this.target_x = HIDE_X;
        this.isHidden = true;
        this.textColor.a = 0.0F;
        this.btnColor.a = 0.0F;
    }

    public void hide()
    {
        this.isHidden = true;
    }

    public void show()
    {
        this.showTimer = 0.5f;
        this.textColor.a = 0.0F;
        this.btnColor.a = 1;//0.0F;
        this.current_x = SHOW_X; //HIDE_X;
        this.target_x = SHOW_X;
        this.hb.move(SHOW_X, SHOW_Y);
//        this.isHidden = false;
//        this.textColor.a = 0.0F;
//        this.btnColor.a = 0.0F;
//        this.current_x = HIDE_X;
//        this.target_x = SHOW_X;
//        this.hb.move(SHOW_X, SHOW_Y);
    }

    public void render(SpriteBatch sb)
    {
        if (!this.isHidden)
        {
            this.renderButton(sb);

            String text = GetBanishText();
            if (FontHelper.getSmartWidth(FontHelper.smallDialogOptionFont, text, 9999.0F, 0.0F) > 200.0F * Settings.scale)
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, text, this.current_x, SHOW_Y, this.textColor, 0.8F);
            }
            else
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, text, this.current_x, SHOW_Y, this.textColor);
            }
        }
    }

    private void renderButton(SpriteBatch sb)
    {
        float width   = 512f;
        float height  = 256f;
        float originX = 256f;
        float originY = 128f;
        float scale = Settings.scale * 0.75f;

        sb.setColor(this.btnColor);
        sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - originX, SHOW_Y - originY, originX, originY, width, height,
                scale, scale, 0.0F, 0, 0, (int)width, (int)height, false, false);

        if (this.hb.hovered && !this.hb.clickStarted)
        {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
            sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - originX, SHOW_Y - originY, originX, originY, width, height,
                    scale, scale, 0.0F, 0, 0, (int)width, (int)height, false, false);

            sb.setBlendFunction(770, 771);
        }

        this.hb.render(sb);
    }

    private String GetBanishText()
    {
        if (card instanceof HigakiRinne)
        {
            return "...";
        }
        else
        {
            return GR.Animator.Strings.Rewards.Banish;
        }
    }

    static
    {
//        SHOW_Y = 220.0F * Settings.scale;
//        SHOW_X = (float) Settings.WIDTH / 2.0F;
//        HIDE_X = (float) Settings.WIDTH / 2.0F;
        HITBOX_W = 200.0F * Settings.scale;
        HITBOX_H = 70.0F * Settings.scale;
    }
}
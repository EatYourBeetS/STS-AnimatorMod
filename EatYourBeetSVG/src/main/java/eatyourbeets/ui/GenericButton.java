package eatyourbeets.ui;

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
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.AnimatorResources;

public class GenericButton
{
    private float showTimer;
    private boolean isHidden;

    public String text;
    public Color textColor;
    public Color btnColor;
    public Hitbox hb;
    public boolean clicked;

    public GenericButton(Hitbox hitbox)
    {
        this.showTimer = -1;

        this.isHidden = true;
        this.textColor = Color.WHITE.cpy();
        this.btnColor = Color.TAN.cpy();
        this.hb = hitbox;
    }

    public void update()
    {
        if (showTimer >= 0)
        {
            showTimer -= Gdx.graphics.getDeltaTime();
            if (showTimer < 0)
            {
                this.isHidden = false;
            }
        }
        else if (!this.isHidden)
        {
            this.hb.update();
            if (this.hb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.hb.hovered && InputHelper.justClickedLeft)
            {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (this.hb.clicked || InputActionSet.cancel.isJustPressed() || CInputActionSet.cancel.isJustPressed())
            {
                this.hb.clicked = false;
                this.clicked = true;
            }

            this.textColor.a = MathHelper.fadeLerpSnap(this.textColor.a, 1.0F);
            this.btnColor.a = this.textColor.a;
        }
    }

    public void hideInstantly()
    {
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
        this.btnColor.a = 0.0F;
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
            if (FontHelper.getSmartWidth(FontHelper.smallDialogOptionFont, text, 9999.0F, 0.0F) > 200.0F * Settings.scale)
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, text, this.hb.cX, this.hb.cY, this.textColor, 0.8F);
            }
            else
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, text, this.hb.cX, this.hb.cY, this.textColor);
            }
        }
    }

    private void renderButton(SpriteBatch sb)
    {
        float width   = 512f;
        float height  = 256f;
        float originX = 256f;
        float originY = 128f;
        float scale = Settings.scale * 0.6f;

        sb.setColor(this.btnColor);
        sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.hb.cX- originX, this.hb.cY - originY, originX, originY, width, height,
                scale, scale, 0.0F, 0, 0, (int)width, (int)height, false, false);

        if (this.hb.hovered && !this.hb.clickStarted)
        {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
            sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.hb.cX - originX, this.hb.cY - originY, originX, originY, width, height,
                    scale, scale, 0.0F, 0, 0, (int)width, (int)height, false, false);

            sb.setBlendFunction(770, 771);
        }

        this.hb.render(sb);
    }
}
package pinacolada.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.GotItButton;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLRenderHelpers;

public class GUI_Ftue extends FtueTip //TODO
{
    private static final Color BACKDROP = new Color(0.2f, 0.2f, 0.2f, 0.8f);

    private GotItButton button;
    private float x;
    private float y;
    private String header;
    private String body;
    public FtueTip.TipType type = null;

    public GUI_Ftue(String header, String body, float x, float y, FtueTip.TipType type)
    {
        this.openScreen(header, body, x, y);
        this.type = type;
    }

    public void openScreen(String header, String body, float x, float y) {
        this.header = header;
        this.body = body;
        this.x = x;
        this.y = y;
        this.button = new GotItButton(x, y);
        AbstractDungeon.player.releaseCard();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();
    }

    @Override
    public void update() {
        this.button.update();
        if (this.button.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            CardCrawlGame.sound.play("DECK_OPEN");
            if (this.type == FtueTip.TipType.POWER) {
                AbstractDungeon.cardRewardScreen.reopen();
            } else {
                AbstractDungeon.closeCurrentScreen();
            }
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        PCLRenderHelpers.DrawCentered(sb, BACKDROP, GR.PCL.Images.Panel_Rounded.Texture(), x, y, Settings.WIDTH * 0.3f, Settings.HEIGHT * 0.2f, Settings.scale, 0, false, false);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.7F + (MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L)) + 1.25F) / 5.0F));
        this.button.render(sb);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, LABEL[0] + this.header, this.x - 190.0F * Settings.scale, this.y + 130.0F * Settings.scale, Settings.GOLD_COLOR);
        PCLRenderHelpers.WriteSmartText(sb, FontHelper.tipBodyFont, this.body, this.x - 230.0F * Settings.scale, this.y + 80.0F * Settings.scale, 450.0F * Settings.scale, 26.0F * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, LABEL[1], this.x + 194.0F * Settings.scale, this.y - 150.0F * Settings.scale, Settings.GOLD_COLOR);

        if (Settings.isControllerMode) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.proceed.getKeyImg(), this.button.hb.cX - 32.0F + 130.0F * Settings.scale, this.button.hb.cY - 32.0F + 2.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

    }
}

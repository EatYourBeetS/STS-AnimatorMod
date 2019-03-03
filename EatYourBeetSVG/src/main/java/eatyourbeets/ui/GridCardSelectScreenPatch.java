package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import java.util.function.Function;

public class GridCardSelectScreenPatch
{
    public static Hitbox startingCardsSelectedHb;
    public static Hitbox startingCardsLeftHb;
    public static Hitbox startingCardsRightHb;

    private static boolean renderChoice;
    private static Function<Integer, String> onClick;
    private static AbstractCard card;
    private static String currentText;

    public static void Load(AbstractCard card, Function<Integer, String> onClick)
    {
        GridCardSelectScreenPatch.card = card;
        GridCardSelectScreenPatch.onClick = onClick;
        renderChoice = true;
        UpdateCurrentText(0);
    }

    public static void Open(GridCardSelectScreen selectScreen)
    {
        startingCardsSelectedHb = new Hitbox(AbstractCard.IMG_WIDTH, 60.0F * Settings.scale);
        startingCardsLeftHb = new Hitbox(90.0F * Settings.scale, 60.0F * Settings.scale);
        startingCardsRightHb = new Hitbox(90.0F * Settings.scale, 60.0F * Settings.scale);

        if (!renderChoice)
        {
            card = null;
            onClick = null;
        }
        else
        {
            renderChoice = false;
        }
    }

    public static void Update(GridCardSelectScreen selectScreen)
    {
        if (card == null)
        {
            return;
        }

        if (selectScreen.selectedCards.size() > 0)
        {
            card = null;
            onClick = null;
            return;
        }

        startingCardsSelectedHb.update();
        startingCardsRightHb.update();
        startingCardsLeftHb.update();

        if (InputHelper.justClickedLeft)
        {
            if (startingCardsRightHb.hovered)
            {
                startingCardsRightHb.clickStarted = true;
            }
            else if (startingCardsLeftHb.hovered)
            {
                startingCardsLeftHb.clickStarted = true;
            }
        }

        float midPointX = card.current_x + (AbstractCard.IMG_WIDTH * 1.1f);
        float midPointY = card.current_y;
        float offsetY = startingCardsSelectedHb.height / 2f;
        float offsetX = startingCardsLeftHb.width;

        startingCardsSelectedHb.move(midPointX, midPointY + offsetY);
        startingCardsLeftHb.move(midPointX - offsetX, midPointY - offsetY);
        startingCardsRightHb.move(midPointX + offsetX, midPointY - offsetY);

        if (startingCardsLeftHb.clicked)
        {
            startingCardsLeftHb.clicked = false;
            UpdateCurrentText(-1);
        }

        if (startingCardsRightHb.clicked)
        {
            startingCardsRightHb.clicked = false;
            UpdateCurrentText(+1);
        }
    }

    public static void Render(GridCardSelectScreen selectScreen, SpriteBatch sb)
    {
        if (card == null)
        {
            return;
        }

        RenderBox(sb);

        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, currentText, startingCardsSelectedHb.cX, startingCardsSelectedHb.cY - 5 * Settings.scale, Settings.CREAM_COLOR);//.BLUE_TEXT_COLOR);

        if (!startingCardsLeftHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_LEFT_ARROW, startingCardsLeftHb.cX - 24.0F, startingCardsLeftHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        if (!startingCardsRightHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_RIGHT_ARROW, startingCardsRightHb.cX - 24.0F, startingCardsRightHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        startingCardsSelectedHb.render(sb);
        startingCardsLeftHb.render(sb);
        startingCardsRightHb.render(sb);
    }

    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final float h = 40 * Settings.scale;

    private static void RenderBox(SpriteBatch sb)
    {
        float x = startingCardsSelectedHb.x;
        float y = startingCardsSelectedHb.cY;

        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
    }

    private static void UpdateCurrentText(int value)
    {
        currentText = onClick.apply(value);
    }
}

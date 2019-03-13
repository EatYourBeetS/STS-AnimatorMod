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
    public static Hitbox effectTextHb;
    public static Hitbox effectDescriptionHb;
    public static Hitbox leftSelectionHb;
    public static Hitbox rightSelectionHb;

    private static boolean renderChoice;
    private static Function<Integer, String> onClick;
    private static AbstractCard card;
    private static String currentText;
    private static String currentDescription;

    public static void SetDescription(String description)
    {
        currentDescription = description;
    }

    public static void Load(AbstractCard card, Function<Integer, String> onClick)
    {
        GridCardSelectScreenPatch.card = card;
        GridCardSelectScreenPatch.onClick = onClick;
        renderChoice = true;
        currentDescription = "";
        UpdateCurrentText(0);
    }

    public static void Open(GridCardSelectScreen selectScreen)
    {
        effectDescriptionHb = new Hitbox(AbstractCard.IMG_WIDTH, 60.0F * Settings.scale);
        effectTextHb = new Hitbox(AbstractCard.IMG_WIDTH, 60.0F * Settings.scale);
        leftSelectionHb = new Hitbox(120.0F * Settings.scale, 60.0F * Settings.scale);
        rightSelectionHb = new Hitbox(120.0F * Settings.scale, 60.0F * Settings.scale);

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

        effectTextHb.update();
        rightSelectionHb.update();
        leftSelectionHb.update();

        if (InputHelper.justClickedLeft)
        {
            if (rightSelectionHb.hovered)
            {
                rightSelectionHb.clickStarted = true;
            }
            else if (leftSelectionHb.hovered)
            {
                leftSelectionHb.clickStarted = true;
            }
        }

        float midPointX = card.current_x + (AbstractCard.IMG_WIDTH * 1.1f);
        float midPointY = card.current_y;
        float offsetY = effectTextHb.height / 2f;
        float offsetX = leftSelectionHb.width;

        effectDescriptionHb.move(midPointX, midPointY + offsetY);
        effectTextHb.move(midPointX, midPointY - offsetY);
        leftSelectionHb.move(midPointX - offsetX, midPointY - offsetY);
        rightSelectionHb.move(midPointX + offsetX, midPointY - offsetY);

        if (leftSelectionHb.clicked)
        {
            leftSelectionHb.clicked = false;
            UpdateCurrentText(-1);
        }

        if (rightSelectionHb.clicked)
        {
            rightSelectionHb.clicked = false;
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

        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, currentDescription, effectDescriptionHb.cX, effectDescriptionHb.cY - 5 * Settings.scale, Settings.BLUE_TEXT_COLOR, 0.8f);

        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, currentText, effectTextHb.cX, effectTextHb.cY, Settings.CREAM_COLOR);

        if (!leftSelectionHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_LEFT_ARROW, leftSelectionHb.cX - 24.0F, leftSelectionHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        if (!rightSelectionHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(ImageMaster.CF_RIGHT_ARROW, rightSelectionHb.cX - 24.0F, rightSelectionHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        effectTextHb.render(sb);
        leftSelectionHb.render(sb);
        rightSelectionHb.render(sb);
    }

    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final float h = 40 * Settings.scale;

    private static void RenderBox(SpriteBatch sb)
    {
        float x = effectDescriptionHb.x - (5 * Settings.scale);
        float y = effectDescriptionHb.cY;

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

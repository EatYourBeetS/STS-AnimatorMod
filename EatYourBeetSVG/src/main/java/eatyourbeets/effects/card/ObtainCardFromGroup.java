package eatyourbeets.effects.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.utilities.GameEffects;

import java.util.Iterator;

public class ObtainCardFromGroup extends AbstractGameEffect
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private boolean openedScreen = false;
    private CardGroup group;
    private int numChoices;
    private boolean canCancel = false;
    private Color screenColor;

    public ObtainCardFromGroup(CardGroup group, int numChoices)
    {
        this.group = group;
        this.numChoices = numChoices;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;

        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    public ObtainCardFromGroup(CardGroup group, int numChoices, boolean canCancel)
    {
        this (group, numChoices);

        this.canCancel = canCancel;

        if (!canCancel) {
            AbstractDungeon.overlayMenu.proceedButton.hide();
        }

        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.updateBlackScreenColor();

        Iterator var1;
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

            while (var1.hasNext())
            {
                AbstractCard c = (AbstractCard) var1.next();
                GameEffects.Queue.ShowAndObtain(c);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            this.isDone = true;
            //((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
        }

        if (this.duration < 1f && !this.openedScreen)
        {
            this.openedScreen = true;

            AbstractDungeon.gridSelectScreen.open(group, numChoices, TEXT[2], false, false, canCancel
                    , false);
        }
    }

    private void updateBlackScreenColor()
    {
        if (this.duration > 1f)
        {
            this.screenColor.a = Interpolation.fade.apply(1f, 0f, (this.duration - 1f) * 2f);
        }
        else
        {
            this.screenColor.a = Interpolation.fade.apply(0f, 1f, this.duration / 1.5f);
        }

    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID)
        {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void dispose()
    {
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("RewardItem");
        TEXT = uiStrings.TEXT;
    }
}

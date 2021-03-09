package eatyourbeets.effects.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.EmptyCage;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.utilities.GameEffects;

public class MaskedTravelerTransformCardsEffect extends EYBEffectWithCallback<Object>
{
    private static final float DUR = 1.5f;
    private boolean openedPanel = false;
    private final Color screenColor;
    private final String purgeMessage;
    private int cardsToRemove;

    public MaskedTravelerTransformCardsEffect(int transform)
    {
        this.purgeMessage = CardCrawlGame.languagePack.getRelicStrings(EmptyCage.ID).DESCRIPTIONS[1];
        this.cardsToRemove = transform;
        this.duration = 1.5f;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update()
    {
        if (!openedPanel)
        {
            OpenPanel();
        }
        else if (cardsToRemove > 0 && AbstractDungeon.gridSelectScreen.selectedCards.size() == cardsToRemove)
        {
            float displayCount = 0f;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.masterDeck.removeCard(card);

                AbstractCard reward = AbstractDungeon.returnTrulyRandomCard();
                if (reward != null)
                {
                    reward = reward.makeCopy();
                    reward.upgrade();

                    GameEffects.TopLevelQueue.Add(new ShowCardAndObtainEffect(reward, (float) Settings.WIDTH / 3f + displayCount, (float)Settings.HEIGHT / 2f, false));
                    displayCount += (float)Settings.WIDTH / 6f;
                }
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            cardsToRemove = 0;

            Complete(this);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID)
        {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void OpenPanel()
    {
        CardGroup cardGroup = player.masterDeck.getPurgeableCards();
        if (cardGroup.size() < cardsToRemove)
        {
            Complete(this);
            return;
        }

        if (AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(cardGroup, cardsToRemove, purgeMessage, false, false, false, true);
        openedPanel = true;
    }
}

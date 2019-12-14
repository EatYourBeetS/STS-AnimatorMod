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
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class MaskedTravelerTransformCardsEffect extends AbstractGameEffect
{
    private static final float DUR = 1.5F;
    private boolean openedPanel = false;
    private final Color screenColor;
    private final String purgeMessage;
    private int cardsToRemove;

    public MaskedTravelerTransformCardsEffect(int transform)
    {
        this.purgeMessage = CardCrawlGame.languagePack.getRelicStrings(EmptyCage.ID).DESCRIPTIONS[1];
        this.cardsToRemove = transform;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
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
            //AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (float) Settings.WIDTH / 2.0F - 30.0F * Settings.scale - AbstractCard.IMG_WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            //AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(1), (float) Settings.WIDTH / 2.0F + 30.0F * Settings.scale + AbstractCard.IMG_WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));

            float displayCount = 0.0F;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.masterDeck.removeCard(card);

                AbstractCard reward = AbstractDungeon.returnTrulyRandomCard();
                if (reward != null)
                {
                    reward = reward.makeCopy();
                    reward.upgrade();

                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(reward, (float) Settings.WIDTH / 3.0F + displayCount, (float)Settings.HEIGHT / 2.0F, false));
                    displayCount += (float)Settings.WIDTH / 6.0F;
                }
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            cardsToRemove = 0;
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == CurrentScreen.GRID)
        {
            AbstractDungeon.gridSelectScreen.render(sb);
        }

    }

    public void dispose()
    {
    }

    public void OpenPanel()
    {
        CardGroup cardGroup = AbstractDungeon.player.masterDeck.getPurgeableCards();
        if (cardGroup.size() < cardsToRemove)
        {
            this.isDone = true;
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

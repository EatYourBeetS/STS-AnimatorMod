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
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class MaskedTravelerTransformCardsEffect extends EYBEffectWithCallback<Object>
{
    private static final int GROUP_SIZE = 3;
    private final RandomizedList<AbstractCard> cards = new RandomizedList<>();
    private final String purgeMessage;
    private final Color screenColor;
    private boolean openedPanel = false;
    private int cardsToRemove;
    private int cardsToAdd;

    public MaskedTravelerTransformCardsEffect(int remove, int obtain)
    {
        super(0.75f, true);

        this.purgeMessage = CardCrawlGame.languagePack.getRelicStrings(EmptyCage.ID).DESCRIPTIONS[1];
        this.cardsToAdd = obtain;
        this.cardsToRemove = remove;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (cardsToRemove > 0 && cardsToAdd > 0)
        {
            OpenPanel_Remove();
        }
        else
        {
            Complete();
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (cardsToRemove > 0)
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == cardsToRemove)
            {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    AbstractDungeon.player.masterDeck.removeCard(card);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                cardsToRemove = 0;
                OpenPanel_Add();
            }
        }
        else if (cardsToAdd > 0)
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 1)
            {
                float displayCount = 0f;
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    GameEffects.Queue.Add(new ShowCardAndObtainEffect(card.makeCopy(), (float) Settings.WIDTH / 3f + displayCount, (float) Settings.HEIGHT / 2f, false));
                    displayCount += (float) Settings.WIDTH / 6f;
                }

                cardsToAdd -= 1;
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.gridSelectScreen.targetGroup.clear();
                OpenPanel_Add();

                if (cardsToAdd <= 0)
                {
                    AbstractDungeon.closeCurrentScreen();
                }
            }
        }
        else if (TickDuration(deltaTime))
        {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
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

    public void OpenPanel_Remove()
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

    public void OpenPanel_Add()
    {
        final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (cards.Size() == 0)
        {
            cards.AddAll(AbstractDungeon.uncommonCardPool.group);
            if (cards.Size() < cardsToAdd)
            {
                JUtils.LogWarning(this, "Not enough uncommon cards in the card pool");
                Complete(this);
                return;
            }
        }

        for (int i = 0; i < GROUP_SIZE; i++)
        {
            cardGroup.group.add(cards.Retrieve(AbstractDungeon.cardRandomRng, true).makeCopy());
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(cardGroup, cardsToRemove, GR.Common.Strings.GridSelection.ChooseOneCard, false, false, false, false);
        openedPanel = true;
    }
}

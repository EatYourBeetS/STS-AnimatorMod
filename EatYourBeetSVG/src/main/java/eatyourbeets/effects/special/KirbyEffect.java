package eatyourbeets.effects.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.animator.beta.colorless.Kirby;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.utilities.RandomizedList;

public class KirbyEffect extends EYBEffectWithCallback<Kirby>
{
    private static final int GROUP_SIZE = 3;
    private final Kirby kirby;
    private final RandomizedList<AbstractCard> cards = new RandomizedList<>();
    private final String purgeMessage;
    private final Color screenColor;
    private int cardsToRemove;

    public KirbyEffect(Kirby kirby, int remove)
    {
        super(0.75f, true);

        this.kirby = kirby;
        this.purgeMessage = kirby.cardData.Strings.EXTENDED_DESCRIPTION[0];
        this.cardsToRemove = remove;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0f;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (cardsToRemove > 0)
        {
            for (AbstractCard card : kirby.RemoveInheritedCards()) {
                AbstractDungeon.player.masterDeck.addToBottom(card);
            }
            OpenPanel_Remove();
        }
        else
        {
            Complete(kirby);
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
                    AbstractCard newCard = card.makeCopy();
                    for (int i = 0; i < kirby.timesUpgraded; i++) {
                        newCard.upgrade();
                    }
                    kirby.AddInheritedCard(newCard);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                kirby.refreshDescription();
                cardsToRemove = 0;
            }
        }
        else if (TickDuration(deltaTime))
        {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            Complete(kirby);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
        {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void OpenPanel_Remove()
    {
        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : player.masterDeck.getPurgeableCards().group)
        {
            if (!isBanned(c))
            {
                cardGroup.group.add(c);
            }
        }
        if (cardGroup.size() < cardsToRemove)
        {
            Complete(kirby);
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
    }

    public static boolean isBanned(AbstractCard c) {
        return c.cost < 0
                || (c.rarity != AbstractCard.CardRarity.COMMON && c.rarity != AbstractCard.CardRarity.BASIC)
                || c.purgeOnUse
                || c instanceof AnimatorCard_UltraRare
                || c instanceof Kirby
                || c.cardID.startsWith("hubris")
                ||  c.cardID.startsWith("ReplayTheSpireMod")
                ||  c.cardID.startsWith("infinitespire")
                ||  c.cardID.startsWith("StuffTheSpire");
    }
}
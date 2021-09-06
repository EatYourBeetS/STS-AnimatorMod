package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.animator.beta.colorless.AbstractMysteryCard;
import eatyourbeets.cards.animator.beta.colorless.Kirby;
import eatyourbeets.cards.animator.beta.colorless.MysteryCard;
import eatyourbeets.cards.animator.beta.colorless.MysteryCard2;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.ui.GridCardSelectScreenPatch;

import java.util.ArrayList;

public class KirbyAction extends EYBActionWithCallback<Kirby>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    private final Kirby kirby;
    private final String purgeMessage;

    public KirbyAction(Kirby kirby, int amount, int descIndex)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FAST);

        this.kirby = kirby;
        this.purgeMessage = kirby.cardData.Strings.EXTENDED_DESCRIPTION[descIndex];
        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        GridCardSelectScreenPatch.Clear();

        CardGroup cardGroupPlaceholder = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        AbstractMysteryCard placeholder = kirby.GetCard(0) instanceof MysteryCard ? new MysteryCard2(true) : new MysteryCard(true);
        placeholder.isSeen = true;
        cardGroupPlaceholder.addToBottom(placeholder);
        GridCardSelectScreenPatch.AddGroup(cardGroupPlaceholder);


        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        for (AbstractCard c : player.masterDeck.getPurgeableCards().group)
        {
            if (!isBanned(c))
            {
                cardGroup.addToBottom(c);
            }
        }
        GridCardSelectScreenPatch.AddGroup(cardGroup);

        CardGroup mergedGroup = GridCardSelectScreenPatch.GetCardGroup();

        if (mergedGroup.size() < amount)
        {
            GridCardSelectScreenPatch.Clear();
            Complete(kirby);
            return;
        }

        AbstractDungeon.gridSelectScreen.open(mergedGroup, amount, purgeMessage, false, false , false, true);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                AbstractDungeon.player.masterDeck.removeCard(card);
                AbstractCard newCard = card.makeCopy();
                kirby.AddInheritedCard(newCard);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenPatch.Clear();
            kirby.refreshDescription();
            Complete(kirby);
        }
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


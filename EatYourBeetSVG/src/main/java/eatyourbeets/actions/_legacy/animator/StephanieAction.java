package eatyourbeets.actions._legacy.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;

import java.util.List;

public class StephanieAction extends AbstractGameAction
{
    private final AbstractPlayer player;
    private final int cardDraw;

    public StephanieAction(AbstractCreature target, int cardDraw)
    {
        this.target = target;
        this.cardDraw = cardDraw;
        this.player = (AbstractPlayer)target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (this.player.hand.size() == 0)
            {
                this.isDone = true;
            }
            else
            {
                String fetchMessage = Resources_Animator_Strings.Actions.TEXT[0];
                AbstractDungeon.handCardSelectScreen.open(fetchMessage, 1, false, false, false, false, false);
                this.tickDuration();
            }

            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            AbstractPlayer p = AbstractDungeon.player;

            AbstractCard card = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();

            AbstractDungeon.player.hand.addToTop(card);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;

            AnimatorCard selectedCard = JavaUtilities.SafeCast(card, AnimatorCard.class);
            if (selectedCard == null)
            {
                this.isDone = true;
                return;
            }

            GameActionsHelper_Legacy.AddToTop(new FetchAction(player.drawPile, selectedCard::HasSynergy, cardDraw, this::OnFetch));
        }

        this.tickDuration();
    }

    private void OnFetch(List<AbstractCard> cards)
    {
        if (cards != null && cards.size() > 0)
        {
            for (AbstractCard c : cards)
            {
                c.triggerWhenDrawn();
                PlayerStatistics.Instance.OnAfterDraw(c);
            }
        }
    }
}
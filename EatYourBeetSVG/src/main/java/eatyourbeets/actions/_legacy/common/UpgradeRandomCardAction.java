package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class UpgradeRandomCardAction extends AbstractGameAction
{
    public UpgradeRandomCardAction()
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            RandomizedList<AbstractCard> betterPossible = new RandomizedList<>();
            RandomizedList<AbstractCard> possible = new RandomizedList<>();

            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                if (c.canUpgrade() && !GameUtilities.IsCurseOrStatus(c))
                {
                    if (!c.upgraded)
                    {
                        betterPossible.Add(c);
                    }
                    else
                    {
                        possible.Add(c);
                    }
                }
            }

            AbstractCard card = null;
            if (betterPossible.Count() > 0)
            {
                card = betterPossible.Retrieve(AbstractDungeon.cardRng);
            }
            else if (possible.Count() > 0)
            {
                card = possible.Retrieve(AbstractDungeon.cardRng);
            }

            if (card != null)
            {
                card.upgrade();
                card.flash();
            }
        }

        this.tickDuration();
    }
}

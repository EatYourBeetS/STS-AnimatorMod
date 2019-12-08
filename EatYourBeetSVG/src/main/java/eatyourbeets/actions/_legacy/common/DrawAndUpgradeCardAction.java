package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;

public class DrawAndUpgradeCardAction extends AbstractGameAction
{
    private final int count;

    public DrawAndUpgradeCardAction(AbstractCreature target, int count)
    {
        this.target = target;
        this.count = count;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        DrawUpgradableCard(this.count);
        this.isDone = true;
    }

    private void DrawUpgradableCard(int count)
    {
        AbstractPlayer player = AbstractDungeon.player;
        CardGroup drawPile = player.drawPile;
        for (AbstractCard c : drawPile.getUpgradableCards().group)
        {
            if (c.canUpgrade())
            {
                count -= 1;
                c.upgrade();
                c.flash();

                //AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(c.target_x, c.target_y));
                GameActionsHelper.AddToTop(new RefreshHandLayoutAction());
                GameActionsHelper.AddToTop(new DrawSpecificCardAction(c));

                if (count == 0)
                {
                    return;
                }
            }
        }

        if (drawPile.size() < count && player.discardPile.size() > 0)
        {
            GameActionsHelper.AddToTop(new DrawAndUpgradeCardAction(player, count));
            GameActionsHelper.AddToTop(new EmptyDeckShuffleAction());
        }
        else if (count > 0)
        {
            GameActionsHelper.AddToTop(new DrawCardAction(AbstractDungeon.player, count, false));
        }
    }
}
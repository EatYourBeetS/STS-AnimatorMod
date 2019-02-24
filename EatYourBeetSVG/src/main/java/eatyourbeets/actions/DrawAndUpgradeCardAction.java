package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.GameActionsHelper;

public class DrawAndUpgradeCardAction extends AnimatorAction
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
                AbstractDungeon.actionManager.addToBottom(new DrawSpecificCardAction(c));

                if (count == 0)
                {
                    return;
                }
            }
        }

        if (drawPile.size() < count && player.discardPile.size() > 0)
        {
            GameActionsHelper.AddToBottom(new EmptyDeckShuffleAction());
            GameActionsHelper.AddToBottom(new DrawAndUpgradeCardAction(player, count));
        }
        else if (count > 0)
        {
            GameActionsHelper.DrawCard(AbstractDungeon.player, count);
        }
    }
}
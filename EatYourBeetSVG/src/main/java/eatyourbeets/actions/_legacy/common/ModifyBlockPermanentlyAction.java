package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

public class ModifyBlockPermanentlyAction extends AbstractGameAction
{
    private final int increaseAmount;
    private final UUID uuid;

    public ModifyBlockPermanentlyAction(UUID targetUUID, int incAmount)
    {
        this.increaseAmount = incAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update()
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid.equals(this.uuid))
            {
                c.misc += this.increaseAmount;
                c.applyPowers();
                c.baseBlock = c.misc;
                c.isBlockModified = false;
            }
        }

        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.misc += this.increaseAmount;
            c.applyPowers();
            c.baseBlock = c.misc;
        }

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
        {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }

        this.isDone = true;
    }
}
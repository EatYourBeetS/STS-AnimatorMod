package eatyourbeets.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

public class ModifyDamagePermanentlyAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private int increaseAmount;
    private UUID uuid;

    public ModifyDamagePermanentlyAction(UUID targetUUID, int incAmount)
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
                c.baseDamage = c.misc;
                c.isDamageModified = false;
            }
        }

        for (AbstractCard c : com.megacrit.cardcrawl.helpers.GetAllInBattleInstances.get(this.uuid))
        {
            c.misc += this.increaseAmount;
            c.applyPowers();
            c.baseDamage = c.misc;
        }

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
        {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }

        this.isDone = true;
    }
}
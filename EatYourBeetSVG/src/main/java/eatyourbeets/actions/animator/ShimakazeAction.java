package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShimakazeAction extends com.megacrit.cardcrawl.actions.AbstractGameAction
{
    private final AbstractPlayer source;
    private final int damage;

    public ShimakazeAction(AbstractPlayer source, AbstractCreature target, int damage)
    {
        this.target = target;
        this.source = source;
        this.duration = DEFAULT_DURATION;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.damage = damage;
    }

    public void update()
    {
        if (source.drawPile.isEmpty())
        {
            this.isDone = true;
            return;
        }

        AbstractCard card = source.drawPile.getTopCard();
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, new DamageInfo(source, damage), AttackEffect.SLASH_DIAGONAL));
        }

        this.isDone = true;
    }
}

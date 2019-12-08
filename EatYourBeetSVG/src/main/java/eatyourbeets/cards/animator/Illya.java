package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.actions._legacy.common.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;

public class Illya extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(Illya.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Illya()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 6, 8);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new IllyaAction(m));
        GameActionsHelper.ApplyPower(p, p, new SelfDamagePower(p, magicNumber), magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-2);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (!DrawBerserker(p.drawPile))
        {
            if (!DrawBerserker(p.discardPile))
            {
                if (!DrawBerserker(p.exhaustPile))
                {
                    DrawBerserker(p.hand);
                }
            }
        }

        GameActionsHelper.GainEnergy(1);
    }

    private boolean DrawBerserker(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Berserker.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameActionsHelper.AddToTop(new MoveCard(c, AbstractDungeon.player.hand, group, true));
                }

                return true;
            }
        }

        return false;
    }

    private class IllyaAction extends AbstractGameAction
    {
        private final AbstractMonster target;

        private IllyaAction(AbstractMonster target)
        {
            this.target = target;
        }

        @Override
        public void update()
        {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractCard bestCard = null;
            int maxDamage = Integer.MIN_VALUE;
            for (AbstractCard c : p.drawPile.group)
            {
                if (c.type == CardType.ATTACK && c.cardPlayable(target))
                {
                    c.calculateCardDamage(target);
                    if (c.damage > maxDamage)
                    {
                        maxDamage = c.damage;
                        bestCard = c;
                    }
                }
            }

            if (bestCard != null)
            {
                GameActionsHelper.AddToTop(new PlayCardFromPileAction(bestCard, p.drawPile, false, false, target));
            }

            this.isDone = true;
        }
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class MaesHughes extends AnimatorCard implements OnCallbackSubscriber
{
    public static final String ID = Register(MaesHughes.class.getSimpleName(), EYBCardBadge.Exhaust);

    public MaesHughes()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,6, 3);

        SetSynergy(Synergies.FullmetalAlchemist);
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
        int cardDraw = Math.floorDiv(p.drawPile.size(), magicNumber);
        GameActionsHelper2.Draw(cardDraw);
        GameActionsHelper.Motivate(1, 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (!DrawRoyMustang(p.drawPile))
        {
            if (!DrawRoyMustang(p.discardPile))
            {
                if (!DrawRoyMustang(p.exhaustPile))
                {
                    DrawRoyMustang(p.hand);
                }
            }
        }
    }

    private boolean DrawRoyMustang(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (RoyMustang.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameActionsHelper.AddToTop(new MoveCard(c, AbstractDungeon.player.hand, group, true));
                }

                c.modifyCostForTurn(-1);

                return true;
            }
        }

        return false;
    }
}
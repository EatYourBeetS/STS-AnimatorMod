package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.common.DrawSpecificCardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCostRefreshSubscriber;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.ShiroPower;

public class Shiro extends AnimatorCard implements OnCostRefreshSubscriber
{
    public static final String ID = Register(Shiro.class.getSimpleName(), EYBCardBadge.Special);

    private int costModifier = 0;

    public Shiro()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        this.resetAttributes();
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        costModifier = 0;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        costModifier = 0;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        costModifier = 0;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Shiro copy = (Shiro)super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        OnCostRefresh(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractCard c : p.drawPile.group)
        {
            if (Sora.ID.equals(c.cardID))
            {
                GameActions.Top.MoveCard(c, p.hand, p.drawPile, true);

                break;
            }
        }

        GameActions.Bottom.StackPower(new ShiroPower(p, 1));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int currentCost = (costForTurn + costModifier);

            costModifier = PlayerStatistics.getSynergiesThisTurn();

            if (!this.freeToPlayOnce)
            {
                setCostForTurn(currentCost - costModifier);
            }
        }
    }
}
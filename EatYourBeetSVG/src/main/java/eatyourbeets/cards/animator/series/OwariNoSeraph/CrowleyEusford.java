package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class CrowleyEusford extends AnimatorCard implements OnCostRefreshSubscriber
{
    public static final String ID = Register(CrowleyEusford.class, EYBCardBadge.Special);

    private int costModifier = 0;

    public CrowleyEusford()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(18, 0, 4);
        SetUpgrade( 3, 0, 1);

        SetHealing(true);
        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        resetAttributes();
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
        CrowleyEusford copy = (CrowleyEusford) super.makeStatEquivalentCopy();

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
        GameActions.Bottom.Heal(magicNumber);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this)
        {
            int currentCost = (costForTurn + costModifier);

            costModifier = PlayerStatistics.getCardsExhaustedThisTurn();

            if (!this.freeToPlayOnce)
            {
                setCostForTurn(currentCost - costModifier);
            }
        }
    }
}
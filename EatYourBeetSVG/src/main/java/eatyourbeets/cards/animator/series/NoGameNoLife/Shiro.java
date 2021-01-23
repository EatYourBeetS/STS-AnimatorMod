package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ShiroPower;
import eatyourbeets.utilities.GameActions;

public class Shiro extends AnimatorCard implements OnCostRefreshSubscriber
{
    public static final EYBCardData DATA = Register(Shiro.class).SetPower(4, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Sora(), true);
    }

    private int costModifier = 0;

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        this.resetAttributes();
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        Refresh(null);
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
        Shiro copy = (Shiro) super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        OnCostRefresh(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.Draw(1)
        .ShuffleIfEmpty(false)
        .SetFilter(c -> Sora.DATA.ID.equals(c.cardID), false);

        GameActions.Bottom.StackPower(new ShiroPower(p, 1));
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this && !player.limbo.contains(this))
        {
            int currentCost = (costForTurn + costModifier);

            costModifier = CombatStats.SynergiesThisTurn().size();

            if (!this.freeToPlayOnce)
            {
                setCostForTurn(currentCost - costModifier);
            }
        }
    }
}
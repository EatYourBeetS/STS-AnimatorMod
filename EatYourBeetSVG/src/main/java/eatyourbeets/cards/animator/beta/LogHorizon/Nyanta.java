package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.NoGameNoLife.Shiro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnCostRefreshSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Nyanta extends AnimatorCard implements OnCostRefreshSubscriber {
    public static final EYBCardData DATA = Register(Nyanta.class).SetAttack(6, CardRarity.UNCOMMON, EYBAttackType.Piercing);

    private int costModifier = 0;

    public Nyanta() {
        super(DATA);

        Initialize(3, 0, 3,1);
        SetUpgrade(1, 0, 0);
        SetRetain(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void resetAttributes()
    {
        super.resetAttributes();

        costModifier = 0;
    }

    @Override
    public void OnCostRefresh(AbstractCard card)
    {
        if (card == this && !player.limbo.contains(this))
        {
            int currentCost = (costForTurn + costModifier);

            costModifier = CombatStats.SynergiesThisTurn();

            if (!this.freeToPlayOnce)
            {
                setCostForTurn(currentCost - costModifier);
            }
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Nyanta copy = (Nyanta) super.makeStatEquivalentCopy();

        copy.costModifier = this.costModifier;

        return copy;
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float damage)
    {
        if (GameUtilities.IsInStance(AgilityStance.STANCE_ID))
        {
            damage = baseDamage + CombatStats.SynergiesThisTurn();
        }

        return super.ModifyDamage(enemy, damage);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetVFX(true, false);
        }
    }
}
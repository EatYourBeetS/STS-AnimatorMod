package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Kazuma extends AnimatorCard
{
    public static final String ID = Register(Kazuma.class.getSimpleName(), EYBCardBadge.Synergy);

    public Kazuma()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 6, 4);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Cycle(name, 1);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.DealDamageToRandomEnemy(magicNumber, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetPiercing(true, false);
        }
    }
}
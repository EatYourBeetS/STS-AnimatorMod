package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class Kazuma extends AnimatorCard
{
    public static final String ID = Register(Kazuma.class.getSimpleName(), EYBCardBadge.Synergy);

    public Kazuma()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,6, 4);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Cycle(1, name);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.DealDamageToRandomEnemy(magicNumber, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
            .SetOptions(true, false);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.actions._legacy.common.ExhaustFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Mikaela extends AnimatorCard
{
    public static final String ID = Register(Mikaela.class.getSimpleName(), EYBCardBadge.Special);

    public Mikaela()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7,0, 2, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0 && GameUtilities.IsCurseOrStatus(cards.get(0)))
            {
                GameActions.Bottom.GainForce(secondaryValue);
            }
        });
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }
}
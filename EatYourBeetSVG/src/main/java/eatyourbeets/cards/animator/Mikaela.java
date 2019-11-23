package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ExhaustFromPileAction;
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
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (p.discardPile.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ExhaustFromPileAction(1, false, p.discardPile, false, m, this::OnExhaust));
        }

        GameActionsHelper.GainTemporaryHP(p, magicNumber);
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

    private void OnExhaust(Object state, ArrayList<AbstractCard> exhausted)
    {
        if (state != null && exhausted != null && exhausted.size() > 0)
        {
            AbstractCard card = exhausted.get(0);
            if (card != null && GameUtilities.IsCurseOrStatus(card))
            {
                GameActionsHelper.GainForce(secondaryValue);
            }
        }
    }
}
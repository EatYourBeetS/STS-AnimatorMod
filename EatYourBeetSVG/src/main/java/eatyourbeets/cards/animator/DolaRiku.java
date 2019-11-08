package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.DolaRikuAction;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class DolaRiku extends AnimatorCard
{
    public static final String ID = Register(DolaRiku.class.getSimpleName(), EYBCardBadge.Synergy);

    public DolaRiku()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new DolaRikuAction(p, this.magicNumber));

        if (HasActiveSynergy())
        {
            for (AbstractCard c : p.drawPile.group)
            {
                if (c.costForTurn == 0 && c.type != CardType.CURSE && c.type != CardType.STATUS)
                {
                    GameActionsHelper.AddToTop(new DrawSpecificCardAction(c));
                    return;
                }
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}
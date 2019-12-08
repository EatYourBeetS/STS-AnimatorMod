package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions._legacy.animator.DolaRikuAction;
import eatyourbeets.actions.basic.DrawCards;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.GameUtilities;

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
        GameActionsHelper2.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> GameActionsHelper.AddToBottom(new DolaRikuAction(cards.get(0), magicNumber)));

        if (HasActiveSynergy())
        {
            GameActionsHelper2.AddToTop(new DrawCards(1)
            .SetFilter(c -> c.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(c), false));
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
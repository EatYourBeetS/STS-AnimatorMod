package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.ModifyMagicNumberAction;
import eatyourbeets.actions.animator.WizAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class Wiz extends AnimatorCard
{
    public static final String ID = Register(Wiz.class.getSimpleName(), EYBCardBadge.Synergy);

    public Wiz()
    {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.hand.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ExhaustAction(p, p, 1, false));
            GameActionsHelper.AddToBottom(new WizAction(p));
        }

        if (HasActiveSynergy() && PlayerStatistics.TryActivateLimited(cardID))
        {
            return;
        }

        GameActionsHelper.PurgeCard(this);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}
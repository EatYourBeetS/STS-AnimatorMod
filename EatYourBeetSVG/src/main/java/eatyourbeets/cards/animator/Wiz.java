package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

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
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, true)
        .AddCallback(__ -> GameActions.Bottom.FetchFromPile(name, 1, AbstractDungeon.player.exhaustPile)
                           .SetOptions(false, false)
                           .SetFilter(c -> !c.cardID.equals(Wiz.ID)));

        if (!(HasActiveSynergy() && EffectHistory.TryActivateLimited(cardID)))
        {
            GameActions.Bottom.Purge(this);
        }
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
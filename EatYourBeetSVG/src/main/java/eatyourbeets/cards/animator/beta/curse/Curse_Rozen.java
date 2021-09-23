package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Curse_Rozen extends AnimatorCard_Curse implements OnSynergySubscriber
{
    public static final EYBCardData DATA = Register(Curse_Rozen.class)
    		.SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.RozenMaiden);

    public Curse_Rozen()
    {
        super(DATA, false);

        Initialize(0, 0);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (player.hand.contains(this))
        {
            CombatStats.onSynergy.Subscribe(this);
        }
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        if (player.hand.contains(this))
        {
            flash();
            GameActions.Top.MoveCard(this, player.hand, player.drawPile)
                    .SetDestination(CardSelection.Top);
        }
        CombatStats.onSynergy.Unsubscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}

// Unplayable.
// While in hand, put this on top of your draw pile whenever you Synergize.
package eatyourbeets.cards.animator.beta.RozenMaiden;

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
    		.SetCurse(-2, EYBCardTarget.None);

    public Curse_Rozen()
    {
        super(DATA, false);

        Initialize(0, 0);

        SetSynergy(Synergies.RozenMaiden);
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
    public void OnSynergy(AnimatorCard card)
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }
}

// Unplayable.
// While in hand, put this on top of your draw pile whenever you Synergize.
package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Defend_DateALive extends Defend
{
    public static final String ID = Register(Defend_DateALive.class).ID;

    public Defend_DateALive()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        int totalCards = player.drawPile.size() + player.discardPile.size() + player.hand.size();
        if (totalCards >= 30 && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(2);
        }
    }
}
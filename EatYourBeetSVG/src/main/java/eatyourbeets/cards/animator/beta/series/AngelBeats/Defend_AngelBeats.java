package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_AngelBeats extends Defend
{
    public static final String ID = Register(Defend_AngelBeats.class).ID;

    public Defend_AngelBeats()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ExhaustFromHand(name, magicNumber, false);
        }
    }
}
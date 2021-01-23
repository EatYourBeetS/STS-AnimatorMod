package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_Bleach extends Defend
{
    public static final String ID = Register(Defend_Bleach.class).ID;

    public Defend_Bleach()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire(), true);
        }
    }
}
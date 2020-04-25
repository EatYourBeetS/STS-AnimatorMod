package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;

public class Defend_Rewrite extends Defend
{
    public static final String ID = Register(Defend_Rewrite.class).ID;

    public Defend_Rewrite()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        AgilityPower.PreserveOnce();
    }
}
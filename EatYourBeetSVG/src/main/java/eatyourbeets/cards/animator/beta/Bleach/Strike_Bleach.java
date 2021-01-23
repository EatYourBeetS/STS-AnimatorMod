package eatyourbeets.cards.animator.beta.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Strike_Bleach extends Strike
{
    public static final String ID = Register(Strike_Bleach.class).ID;

    public Strike_Bleach()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0, 2);
        SetUpgrade(9, 0);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainForce(1, false);
        }
    }
}
package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

        Initialize(5, 0, 2);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        if (GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainForce(1, false);
        }
    }
}
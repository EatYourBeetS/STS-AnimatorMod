package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class Strike_Rewrite extends Strike
{
    public static final String ID = Register(Strike_Rewrite.class).ID;

    public Strike_Rewrite()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        ForcePower.PreserveOnce();
    }
}
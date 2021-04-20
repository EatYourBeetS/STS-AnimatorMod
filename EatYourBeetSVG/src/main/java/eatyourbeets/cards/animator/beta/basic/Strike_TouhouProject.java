package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_TouhouProject extends Strike
{
    public static final String ID = Register(Strike_TouhouProject.class).ID;

    public Strike_TouhouProject()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetAttackType(EYBAttackType.Elemental);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
    }
}

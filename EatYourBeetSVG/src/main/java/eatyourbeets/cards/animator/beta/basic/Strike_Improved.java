package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.utilities.GameActions;

public class Strike_Improved extends Strike
{
    public static final String ID = Register(Strike_Improved.class).ID;

    public Strike_Improved()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetShapeshifter();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}
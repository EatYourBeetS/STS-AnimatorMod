package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_Overlord extends Strike
{
    public static final String ID = Register(Strike_Overlord.class).ID;

    public Strike_Overlord()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0, 3);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage += magicNumber);
    }
}
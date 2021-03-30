package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_Elsword extends Strike
{
    public static final String ID = Register(Strike_Elsword.class).ID;

    public Strike_Elsword()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6, 0, 1);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}
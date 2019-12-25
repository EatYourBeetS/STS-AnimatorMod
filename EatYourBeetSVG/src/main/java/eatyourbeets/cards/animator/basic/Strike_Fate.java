package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_Fate extends Strike
{
    public static final String ID = Register(Strike_Fate.class.getSimpleName());

    public Strike_Fate()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int cards = p.hand.size();
        if (p.hand.contains(this))
        {
            cards -= 1;
        }

        if (cards < 3)
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}
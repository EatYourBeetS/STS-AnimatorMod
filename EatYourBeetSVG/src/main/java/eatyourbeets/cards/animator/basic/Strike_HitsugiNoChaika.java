package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_HitsugiNoChaika extends Strike
{
    public static final String ID = Register(Strike_HitsugiNoChaika.class.getSimpleName());

    public Strike_HitsugiNoChaika()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0, 2);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
    }
}
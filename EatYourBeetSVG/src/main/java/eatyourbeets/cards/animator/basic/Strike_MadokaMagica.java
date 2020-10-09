package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_MadokaMagica extends Strike
{
    public static final String ID = Register(Strike_MadokaMagica.class).ID;

    public Strike_MadokaMagica()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0, 2);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.Scry(magicNumber);
        GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}
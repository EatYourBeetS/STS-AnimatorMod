package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;

public class Asuramaru extends AnimatorCard implements Hidden
{
    public static final String ID = Register(Asuramaru.class.getSimpleName());

    public Asuramaru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);

        Initialize(9,0,2);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper2.StackPower(new DemonFormPower(p, magicNumber));
        GameActionsHelper2.GainIntellect(magicNumber);
        GameActionsHelper2.GainAgility(magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(9);
        }
    }
}
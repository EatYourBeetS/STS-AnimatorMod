package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Asuramaru extends AnimatorCard implements Hidden
{
    public static final String ID = Register(Asuramaru.class);

    public Asuramaru()
    {
        super(ID, 2, CardRarity.SPECIAL, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(12, 0, 2);
        SetUpgrade(6, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.StackPower(new DemonFormPower(p, magicNumber));
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
    }
}
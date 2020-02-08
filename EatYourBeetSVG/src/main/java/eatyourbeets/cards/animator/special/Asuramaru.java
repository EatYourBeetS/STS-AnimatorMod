package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Asuramaru.class).SetAttack(2, CardRarity.SPECIAL);

    public Asuramaru()
    {
        super(DATA);

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
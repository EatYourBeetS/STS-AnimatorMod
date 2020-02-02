package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class EntouJyuu extends AnimatorCard
{
    public static final String ID = Register(EntouJyuu.class);

    public EntouJyuu()
    {
        super(ID, 1, CardRarity.SPECIAL, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
    }
}
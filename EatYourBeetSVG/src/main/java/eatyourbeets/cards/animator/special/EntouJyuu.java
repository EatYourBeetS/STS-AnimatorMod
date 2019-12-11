package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class EntouJyuu extends AnimatorCard implements Hidden
{
    public static final String ID = Register(EntouJyuu.class.getSimpleName());

    public EntouJyuu()
    {
        super(ID, 1, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}
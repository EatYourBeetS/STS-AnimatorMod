package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EntouJyuuPower;

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
        GameActionsHelper.GainAgility(magicNumber);
        GameActionsHelper.ApplyPower(p, p, new FlamingWeaponPower(p, 1), 1);
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
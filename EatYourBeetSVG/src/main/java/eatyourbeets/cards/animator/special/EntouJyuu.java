package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class EntouJyuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EntouJyuu.class).SetPower(0, CardRarity.SPECIAL);

    public EntouJyuu()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
        SetAffinity(0, 0, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
    }
}
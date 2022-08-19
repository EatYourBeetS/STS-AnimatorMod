package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.FlamingWeaponPower;
import eatyourbeets.utilities.GameActions;

public class Emonzaemon_EntouJyuu extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Emonzaemon_EntouJyuu.class).SetPower(0, CardRarity.SPECIAL);

    public Emonzaemon_EntouJyuu()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSeries(CardSeries.Katanagatari);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.StackPower(new FlamingWeaponPower(p, 1));
    }
}
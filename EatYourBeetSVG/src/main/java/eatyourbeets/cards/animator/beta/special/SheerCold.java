package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.SheerColdPower;
import eatyourbeets.utilities.GameActions;

public class SheerCold extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SheerCold.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public SheerCold()
    {
        super(DATA);

        Initialize(0, 0, 10, 15);
        SetUpgrade(0, 0, 10, 10);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new SheerColdPower(p, magicNumber, secondaryValue));
    }
}
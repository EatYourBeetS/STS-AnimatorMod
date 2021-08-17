package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.SheerColdPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;

public class SheerCold extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SheerCold.class).SetPower(2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int FREEZING_REDUCTION_BONUS = 10;

    public SheerCold()
    {
        super(DATA);

        Initialize(0, 0, 1, FREEZING_REDUCTION_BONUS);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Blue(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new SheerColdPower(p, magicNumber));
        GameActions.Bottom.Callback(() -> FreezingPower.AddPlayerReductionBonus(FREEZING_REDUCTION_BONUS));
    }
}
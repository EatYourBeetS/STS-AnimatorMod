package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.animator.BlazingHeatPower;
import eatyourbeets.utilities.GameActions;

public class BlazingHeat extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BlazingHeat.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);

    public BlazingHeat()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Red(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Fire::new, magicNumber);
        GameActions.Bottom.StackPower(new BlazingHeatPower(p, 1));
    }
}
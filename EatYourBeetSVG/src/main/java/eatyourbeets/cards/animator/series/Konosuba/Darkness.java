package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.animator.special.DarknessAdrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.DarknessPower;
import eatyourbeets.utilities.GameActions;

public class Darkness extends AnimatorCard
{
    public static final String ID = Register(Darkness.class);

    public Darkness()
    {
        super(ID, 1, CardRarity.COMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0, 2, 2);
        SetUpgrade(0, 1, 1);

        SetSynergy(Synergies.Konosuba);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new DarknessAdrenaline(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new PlatedArmorPower(p, this.magicNumber));
        GameActions.Bottom.StackPower(new DarknessPower(p, 1));
    }
}
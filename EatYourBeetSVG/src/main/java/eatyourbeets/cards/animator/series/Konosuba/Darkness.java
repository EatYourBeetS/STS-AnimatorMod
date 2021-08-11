package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.animator.special.Darkness_Adrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.DarknessPower;
import eatyourbeets.utilities.GameActions;

public class Darkness extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Darkness.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Darkness_Adrenaline(), false);
    }

    public Darkness()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetUpgrade(0, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new PlatedArmorPower(p, magicNumber));
        GameActions.Bottom.StackPower(new DarknessPower(p, 1));
    }
}
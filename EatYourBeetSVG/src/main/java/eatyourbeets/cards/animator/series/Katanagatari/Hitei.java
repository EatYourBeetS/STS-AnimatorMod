package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.HiteiPower;
import eatyourbeets.utilities.GameActions;

public class Hitei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hitei.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, 0, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, upgraded));
    }
}
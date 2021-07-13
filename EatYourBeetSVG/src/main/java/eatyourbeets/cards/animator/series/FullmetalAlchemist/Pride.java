package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.PridePower;
import eatyourbeets.utilities.GameActions;

public class Pride extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Pride.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Pride()
    {
        super(DATA);

        Initialize(0,0, 1, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1, 1, 0);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrbs(Dark::new, magicNumber);
        GameActions.Bottom.ApplyConstricted(p, m, this.secondaryValue);
        GameActions.Bottom.ApplyPower(new PridePower(p));
    }
}
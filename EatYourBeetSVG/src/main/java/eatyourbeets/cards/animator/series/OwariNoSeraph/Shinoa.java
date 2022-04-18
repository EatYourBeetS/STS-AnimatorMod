package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Shinoa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shinoa.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Shinoa()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }
        else
        {
            GameActions.Bottom.GainTemporaryStats(0, magicNumber, 0);
        }
    }
}
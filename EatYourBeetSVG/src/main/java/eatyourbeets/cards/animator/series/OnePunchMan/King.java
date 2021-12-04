package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class King extends AnimatorCard
{
    public static final EYBCardData DATA = Register(King.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public King()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 1, 0, 0);

        SetAffinity_Red(1, 0, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(player), 1).IgnoreArtifact(true);
        if (upgraded) {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.RandomEnemy(player), 1).IgnoreArtifact(true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        if (GameUtilities.TrySpendAffinityPower(Affinity.Light, magicNumber)) {
            GameActions.Bottom.GainMight(magicNumber);
        }
    }
}
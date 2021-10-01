package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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

        Initialize(0, 0, 6, 2);
        SetUpgrade(0, 0, -1, 0);

        SetAffinity_Red(1);
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
        if (GameUtilities.SpendSuperchargedCharge(magicNumber)) {
            GameActions.Bottom.GainForce(secondaryValue);
        }
    }
}
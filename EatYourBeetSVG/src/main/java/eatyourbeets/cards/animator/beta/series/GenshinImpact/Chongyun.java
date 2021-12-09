package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Chongyun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chongyun.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Normal).SetSeriesFromClassPackage(true);

    public Chongyun()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);
        SetAffinity_Red(0,0,1);
        SetAffinity_Blue(1, 0, 0);

        SetAffinityRequirement(Affinity.Blue, 4);
    }

    @Override
    public int GetXValue() {
        return magicNumber + GameUtilities.GetOrbCount(Frost.ORB_ID);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), GetXValue());

        if (TrySpendAffinity(Affinity.Blue)) {
            GameActions.Bottom.ChannelOrb(new Frost());
        }
    }
}
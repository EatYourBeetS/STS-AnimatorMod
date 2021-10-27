package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class LeleiLaLalena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LeleiLaLalena.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public LeleiLaLalena()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0,0,1);

        SetAffinity_Earth();

        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Cycle(name, 1)
            .SetOptions(false, false, false)
            .SetFilter(card -> GameUtilities.HasAffinity(card, Affinity.Earth))
            .AddCallback(() -> GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber));
    }
}
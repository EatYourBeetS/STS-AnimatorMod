package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
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

        Initialize(0, 0, 1);

        SetAffinity_Blue(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        target = HasSynergy() ? CardTarget.SELF_AND_ENEMY : CardTarget.SELF;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackAffinityPower(Affinity.Blue);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber));
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyWeak(TargetHelper.RandomEnemy(), 1);
    }
}
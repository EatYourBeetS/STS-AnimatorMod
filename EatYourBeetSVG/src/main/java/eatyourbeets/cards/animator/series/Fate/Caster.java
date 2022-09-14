package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetDelayed(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetEvokeOrbCount(CheckAffinity(Affinity.Dark) ? 1 : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ReduceStrength(m, magicNumber, false).SetStrengthGain(true);
        GameActions.Bottom.ApplyFrail(info.IsStarter ? p : null, p, magicNumber);
        GameActions.Bottom.GainCorruption(magicNumber);

        if (TryUseAffinity(Affinity.Dark))
        {
            GameActions.Bottom.ChannelOrb(new Dark());
        }
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        GameActions.Bottom.ChannelOrb(new Dark());
    }
}
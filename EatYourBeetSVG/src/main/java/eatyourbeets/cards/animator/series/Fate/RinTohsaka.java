package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class RinTohsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RinTohsaka.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public RinTohsaka()
    {
        super(DATA);

        Initialize(0, 5, 3, 1);
        SetUpgrade(0, 1, 0, 1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);

        final int requirement = 3;
        SetAffinityRequirement(AffinityType.Red, requirement);
        SetAffinityRequirement(AffinityType.Green, requirement);
        SetAffinityRequirement(AffinityType.Blue, requirement);
        SetAffinityRequirement(AffinityType.Light, requirement);
        SetAffinityRequirement(AffinityType.Dark, requirement);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryArtifact(secondaryValue);

        boolean canActivate = true;
        for (AffinityType type : AffinityType.BasicTypes())
        {
            if (!CheckAffinity(type))
            {
                canActivate = false;
            }
        }

        if (canActivate && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrbs(Chaos::new, Math.min(p.orbs.size(), magicNumber));
        }
    }
}
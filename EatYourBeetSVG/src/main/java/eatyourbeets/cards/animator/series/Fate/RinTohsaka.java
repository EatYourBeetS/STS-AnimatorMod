package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
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
        SetAffinityRequirement(Affinity.Red, requirement);
        SetAffinityRequirement(Affinity.Green, requirement);
        SetAffinityRequirement(Affinity.Blue, requirement);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryArtifact(secondaryValue);

        if ((CheckAffinity(Affinity.Red) && CheckAffinity(Affinity.Green) && CheckAffinity(Affinity.Blue)) && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrbs(Chaos::new, Math.min(p.orbs.size(), magicNumber));
        }
    }
}
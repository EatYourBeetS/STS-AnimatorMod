package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class Rider extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rider.class)
            .SetSkill(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    private static final HashSet<CardTags> tagCache = new HashSet<>();

    public Rider()
    {
        super(DATA);

        Initialize(0, 6, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(AffinityType.Green, 3);
        SetAffinityRequirement(AffinityType.Blue, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetRetainOnce(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ReduceStrength(m, magicNumber, true);

        if (CheckAffinity(AffinityType.Green))
        {
            GameActions.Bottom.GainAgility(1, true);
        }

        if (CheckAffinity(AffinityType.Blue))
        {
            GameActions.Bottom.GainIntellect(1, true);
        }
    }
}
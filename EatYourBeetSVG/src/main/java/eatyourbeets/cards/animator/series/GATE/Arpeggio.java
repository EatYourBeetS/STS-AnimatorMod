package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, 10);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Water();
        SetAffinity_Earth();

        SetAffinityRequirement(Affinity.Earth, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainOrbSlots(magicNumber);

        if (CheckAffinity(Affinity.Earth)) {
            GameActions.Bottom.ChannelOrb(new Earth());
            GameActions.Bottom.RaiseEarthLevel(secondaryValue);
        }
    }
}
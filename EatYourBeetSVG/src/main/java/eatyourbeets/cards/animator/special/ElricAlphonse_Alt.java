package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonse_Alt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse_Alt.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetSeries(ElricAlphonse.DATA.Series);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 20, 2, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Earth();
        SetAffinity_Steel();
        SetAffinity_Thunder();

        SetAffinityRequirement(Affinity.Earth, 20);
        SetAffinityRequirement(Affinity.Steel, 20);
        SetAffinityRequirement(Affinity.Thunder, 20);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);

        if (CheckAffinity(Affinity.Earth)) {
            GameActions.Bottom.GainOrbSlots(1);
        }

        if (CheckAffinity(Affinity.Steel)) {
            GameActions.Bottom.GainOrbSlots(1);
        }

        if (CheckAffinity(Affinity.Thunder)) {
            GameActions.Bottom.GainOrbSlots(1);
        }

        GameActions.Bottom.ChannelOrbs(Lightning::new, magicNumber);
    }
}
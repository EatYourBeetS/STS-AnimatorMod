package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonse_Alt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse_Alt.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetSeries(ElricAlphonse.DATA.Series);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Water(1);
        SetAffinity_Fire(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Lightning::new, secondaryValue);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainOrbSlots(1);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }
}
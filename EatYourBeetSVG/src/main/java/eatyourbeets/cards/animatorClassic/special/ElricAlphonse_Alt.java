package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonse_Alt extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse_Alt.class).SetPower(2, CardRarity.SPECIAL);

    public ElricAlphonse_Alt()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetUpgrade(0, 3, 0, 0);

        this.series = CardSeries.FullmetalAlchemist;
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
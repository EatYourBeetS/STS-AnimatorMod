package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonseAlt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonseAlt.class).SetPower(2, CardRarity.SPECIAL);

    public ElricAlphonseAlt()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetUpgrade(0, 3, 0, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrbs(Lightning::new, secondaryValue);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainOrbSlots(1);

        if (HasSynergy())
        {
            GameActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }
}
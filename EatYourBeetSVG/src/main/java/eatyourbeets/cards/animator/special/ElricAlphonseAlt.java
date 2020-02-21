package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ElricAlphonseAlt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricAlphonseAlt.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public ElricAlphonseAlt()
    {
        super(DATA);

        Initialize(0, 6, 3, 2);
        SetUpgrade(0, 2, 1, 0);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.ChannelOrb(new Lightning(), true);
        }

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainPlatedArmor(this.magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}
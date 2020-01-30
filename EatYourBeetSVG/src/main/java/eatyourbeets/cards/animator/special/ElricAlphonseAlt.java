package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class ElricAlphonseAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ElricAlphonseAlt.class);

    public ElricAlphonseAlt()
    {
        super(ID, 1, CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Lightning(), true);
        GameActions.Bottom.GainPlatedArmor(this.magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class ElricAlphonseAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ElricAlphonseAlt.class.getSimpleName(), EYBCardBadge.Synergy);

    public ElricAlphonseAlt()
    {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ChannelOrb(new Lightning(), true);
        GameActions.Bottom.GainPlatedArmor(this.magicNumber);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.GainOrbSlots(1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}
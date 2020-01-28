package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Arpeggio.class, EYBCardBadge.Synergy);

    public Arpeggio()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(secondaryValue);
        }

        GameActions.Bottom.GainIntellect(magicNumber);

        if (HasSynergy())
        {
            GameActions.Bottom.ChannelOrb(new Earth(), true);
        }
    }
}
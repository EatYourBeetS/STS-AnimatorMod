package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Arpeggio.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Arpeggio()
    {
        super(DATA);

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
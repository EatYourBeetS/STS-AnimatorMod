package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Arpeggio.class).SetPower(1, CardRarity.UNCOMMON).SetMaxCopies(2);

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.GainIntellect(secondaryValue, false);

        if (HasSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Earth(), true);
        }
    }
}
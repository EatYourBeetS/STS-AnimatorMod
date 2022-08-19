package eatyourbeets.cards.animatorClassic.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class).SetPower(1, CardRarity.UNCOMMON).SetMaxCopies(2);

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetSeries(CardSeries.GATE);
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.GainIntellect(secondaryValue, false);

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Earth());
        }
    }
}
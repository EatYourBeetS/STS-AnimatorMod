package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class).SetSkill(1, CardRarity.UNCOMMON);

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    public void triggerOnExhaust()
    {
        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        GameActions.Bottom.GainForce(1, false);
        GameActions.Bottom.GainIntellect(1, false);

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Dark(), true);
        }
    }
}
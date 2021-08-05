package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class)
            .SetMaxCopies(2)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetCostUpgrade(-1);

        SetAffinity_Blue(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrb(new Dark());
        GameActions.Bottom.ReduceStrength(m, magicNumber, false).SetStrengthGain(true);
        GameActions.Bottom.ApplyFrail(p, p, secondaryValue);
        GameActions.Bottom.GainCorruption(secondaryValue);
    }
}
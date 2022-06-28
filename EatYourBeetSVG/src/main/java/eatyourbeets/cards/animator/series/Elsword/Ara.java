package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Ara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ara.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Ara()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(2, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.Green, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetSoundPitch(1.1f, 1.3f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetSoundPitch(1.1f, 1.3f);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TryUseAffinity(Affinity.Green))
        {
            GameActions.Bottom.Draw(magicNumber);
        }

        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true)
        .AddCallback(cards ->
        {
            if (cards.size() >= magicNumber)
            {
                GameActions.Bottom.GainAgility(secondaryValue);
            }
        });
    }
}
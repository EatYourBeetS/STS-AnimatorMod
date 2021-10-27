package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.replacement.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActions;

public class AcuraAkari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraAkari.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraAkari()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 1, 0);

        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.CreateThrowingKnives(magicNumber).SetUpgrade(upgraded));

        if (info.IsSynergizing && TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}
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
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
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

        Initialize(0, 0, 2, 1);
        SetCostUpgrade(-1);

        SetAffinity_Air(1);
        SetAffinity_Poison(1);

        SetAffinityRequirement(Affinity.Air, 15);
        SetAffinityRequirement(Affinity.Poison, 15);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, false, false)
        .AddCallback(() -> GameActions.Bottom.CreateThrowingKnives(magicNumber));

        if (CheckAffinity(Affinity.Air) || CheckAffinity(Affinity.Poison))
        {
            GameActions.Bottom.StackPower(new TemporaryEnvenomPower(p, secondaryValue));
        }
    }
}
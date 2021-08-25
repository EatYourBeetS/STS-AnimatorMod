package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Kuroyukihime_BlackLotus;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuroyukihime.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.AccelWorld);
    static
    {
        DATA.AddPreview(new Kuroyukihime_BlackLotus(), false);
    }

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.General, 3);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.Exhaust(this);
            GameActions.Bottom.MakeCardInHand(new Kuroyukihime_BlackLotus())
            .SetUpgrade(CheckAffinity(Affinity.General), false);
        });
    }
}
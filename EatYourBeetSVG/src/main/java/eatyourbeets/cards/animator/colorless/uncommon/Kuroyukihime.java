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
            .SetSeries(CardSeries.AccelWorld)
            .PostInitialize(data -> data.AddPreview(new Kuroyukihime_BlackLotus(), true));

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 1, 2, 1);
        SetCostUpgrade(-1);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Silver(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.General, 8);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlur(secondaryValue);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.MakeCardInHand(new Kuroyukihime_BlackLotus())
            .SetUpgrade(CheckAffinity(Affinity.General), false);
        });
    }
}
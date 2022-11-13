package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Kuroyukihime_BlackLotus;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Kuroyukihime extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Kuroyukihime.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.AccelWorld)
            .PostInitialize(data -> data.AddPreview(new Kuroyukihime_BlackLotus(), false));

    public Kuroyukihime()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.MakeCardInHand(new Kuroyukihime_BlackLotus());
            GameActions.Bottom.Exhaust(this);
        });
    }
}
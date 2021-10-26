package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Yoichi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yoichi.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public Yoichi()
    {
        super(DATA);

        Initialize(0,0, 3);
        SetUpgrade(0,0, 3);

        SetExhaust(true);

        SetAffinity_Air();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .AddCallback(cards -> {
            GameActions.Bottom.GainSupportDamage(magicNumber);
        });
    }
}
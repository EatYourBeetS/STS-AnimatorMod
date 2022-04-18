package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.rare.TanyaDegurechaff;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff_Type95.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(TanyaDegurechaff.DATA.Series);

    public TanyaDegurechaff_Type95()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);

        SetAffinity_Dark(1);
        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (Affinity a : Affinity.Basic())
        {
            GameActions.Bottom.StackAffinityPower(a, magicNumber, false);
        }

        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetFilter(c -> c instanceof EYBCard && ((EYBCard) c).CanScale())
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Top.IncreaseScaling(c, Affinity.Star, secondaryValue);
            }
        });
    }
}
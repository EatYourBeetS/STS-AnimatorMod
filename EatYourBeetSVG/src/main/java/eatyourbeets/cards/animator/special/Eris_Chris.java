package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Eris_Chris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eris_Chris.class)
            .SetAttack(0, CardRarity.SPECIAL)
            .SetSeries(CardSeries.Konosuba);

    public Eris_Chris()
    {
        super(DATA);

        Initialize(4, 0, 4);
        SetUpgrade(2, 0, 2);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Top.Draw(1)
        .SetFilter(card -> card.costForTurn == 0 && !GameUtilities.IsHindrance(card), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.DAGGER).forEach(d -> d.StealGold(magicNumber));
    }
}
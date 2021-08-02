package eatyourbeets.cards.animator.series.Konosuba;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Chris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chris.class)
            .SetAttack(0, CardRarity.UNCOMMON)
            .SetSeries(CardSeries.Konosuba);

    public Chris()
    {
        super(DATA);

        Initialize(4, 0, 4);
        SetUpgrade(2, 0, 2);

        SetAffinity_Green(1, 1, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Top.Draw(1)
        .SetFilter(card -> card.costForTurn == 0 && !GameUtilities.IsCurseOrStatus(card), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).StealGold(magicNumber);
    }
}
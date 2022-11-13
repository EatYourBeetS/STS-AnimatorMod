package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Chris extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Chris.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.UNCOMMON);

    public Chris()
    {
        super(DATA);

        Initialize(4, 0, 4);
        SetUpgrade(2, 0, 2);
        SetScaling(0, 1, 0);

        
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL).StealGold(magicNumber);
    }
}
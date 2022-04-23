package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Chris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Chris.class)
            .SetAttack(0, CardRarity.UNCOMMON)
            .SetSeries(CardSeries.Konosuba);

    public Chris()
    {
        super(DATA);

        Initialize(3, 0, 5, 3);
        SetUpgrade(2, 0, 0, 1);

        SetAffinity_Green(1, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).StealGold(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        });
    }
}
package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class IkkakuBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IkkakuBankai.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeries(CardSeries.Bleach);

    public IkkakuBankai()
    {
        super(DATA);

        Initialize(1, 0, 4);
        SetUpgrade(2, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1, 0, 2);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);

        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                GameActions.Top.DealCardDamageToAll(this, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                        .SetVFX(false, true));
            }
        });
    }
}
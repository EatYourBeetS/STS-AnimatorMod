package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class IkkakuBankai extends PCLCard
{
    public static final PCLCardData DATA = Register(IkkakuBankai.class).SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeries(CardSeries.Bleach);

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
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);

        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                PCLActions.Top.DealCardDamageToAll(this, AttackEffects.SLASH_DIAGONAL).forEach(d -> d
                        .SetVFX(false, true));
            }
        });
    }
}
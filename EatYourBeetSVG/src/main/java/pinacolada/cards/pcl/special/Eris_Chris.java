package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Eris_Chris extends PCLCard
{
    public static final PCLCardData DATA = Register(Eris_Chris.class)
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

        PCLActions.Top.Draw(1)
        .SetFilter(card -> card.costForTurn == 0 && !PCLGameUtilities.IsHindrance(card), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DAGGER).forEach(d -> d.StealGold(magicNumber));
    }
}
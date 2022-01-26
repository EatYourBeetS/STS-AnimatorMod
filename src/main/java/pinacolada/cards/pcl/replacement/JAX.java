package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.utilities.PCLActions;

public class JAX extends PCLCard
{
    public static final PCLCardData DATA = Register(JAX.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public JAX()
    {
        super(DATA);

        Initialize(0, 0, 3, 6);
        SetUpgrade(0, 0, 1);

        SetAffinity_Dark(1);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealDamageAtEndOfTurn(p, p, secondaryValue);
        PCLActions.Bottom.GainMight(magicNumber);
        PCLActions.Bottom.GainDesecration(magicNumber);
    }
}
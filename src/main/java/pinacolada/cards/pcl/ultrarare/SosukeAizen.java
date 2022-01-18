package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.powers.common.CounterAttackPower;
import pinacolada.powers.special.PhasingPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SosukeAizen extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(SosukeAizen.class).SetSkill(-1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Bleach);
    public SosukeAizen()
    {
        super(DATA);

        Initialize(0, 0, 12);
        SetUpgrade(0,0,6);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);

        SetMultiDamage(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int energy = PCLGameUtilities.UseXCostEnergy(this);

        if (energy > 0)
        {
            PCLActions.Bottom.StackPower(new PhasingPower(p, energy));
        }

        CounterAttackPower.retain = true;
        PCLActions.Bottom.StackPower(new CounterAttackPower(p, magicNumber));
    }
}
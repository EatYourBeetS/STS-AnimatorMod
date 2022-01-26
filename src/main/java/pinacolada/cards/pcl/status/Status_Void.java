package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.utilities.PCLActions;

public class Status_Void extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Status_Void.class)
            .SetStatus(-2, CardRarity.COMMON, PCLCardTarget.None);

    public Status_Void()
    {
        super(DATA, false);

        Initialize(0, 0, 2);

        SetEthereal(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (auxiliaryData.form == 0) {
            PCLActions.Bottom.SpendEnergy(1,true);
        }
    }


    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (auxiliaryData.form == 1) {
            PCLActions.Bottom.GainEnergyNextTurn(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}
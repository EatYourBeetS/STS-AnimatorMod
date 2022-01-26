package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.utilities.PCLActions;

public class Status_Burn extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Status_Burn.class)
            .SetStatus(-2, CardRarity.COMMON, PCLCardTarget.AoE);

    public Status_Burn()
    {
        super(DATA, true);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 2, 2);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        SetExhaust(form == 1);
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            PCLActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(),magicNumber);
        }
        else if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.ApplyBurning(null, player, magicNumber);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        PCLActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(),secondaryValue);
    }
}
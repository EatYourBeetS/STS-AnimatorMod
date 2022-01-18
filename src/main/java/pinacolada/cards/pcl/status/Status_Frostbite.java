package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.utilities.PCLActions;

public class Status_Frostbite extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(Status_Frostbite.class).SetMultiformData(2).SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

    public Status_Frostbite()
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
            PCLActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(),magicNumber);
        }
        else if (this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.ApplyFreezing(null, player, magicNumber);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        PCLActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(),secondaryValue);
    }
}
package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Status_Burn extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Burn.class)
            .SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

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
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            GameActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(),magicNumber);
        }
        else if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyBurning(null, player, magicNumber);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        GameActions.Bottom.ApplyBurning(TargetHelper.RandomEnemy(),secondaryValue);
    }
}
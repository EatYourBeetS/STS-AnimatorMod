package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Status_Frostbite extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Frostbite.class).SetMultiformData(2).SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

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
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            GameActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(),magicNumber);
        }
        else if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyFreezing(null, player, magicNumber);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        this.performAction();
    }

    private void performAction() {
        GameActions.Bottom.ApplyFreezing(TargetHelper.RandomEnemy(),secondaryValue);
    }
}
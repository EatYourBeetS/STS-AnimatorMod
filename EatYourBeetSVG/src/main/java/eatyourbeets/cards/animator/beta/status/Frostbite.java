package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Frostbite extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Frostbite.class).SetStatus(-2, CardRarity.COMMON, EYBCardTarget.ALL);

    public Frostbite()
    {
        super(DATA, true);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 1);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
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
        GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(),secondaryValue);
    }
}
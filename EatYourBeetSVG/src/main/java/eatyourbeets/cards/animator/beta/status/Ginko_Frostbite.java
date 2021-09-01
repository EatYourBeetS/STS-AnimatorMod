package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Ginko_Frostbite extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Ginko_Frostbite.class).SetStatus(0, CardRarity.COMMON, EYBCardTarget.ALL);

    public Ginko_Frostbite()
    {
        super(DATA, false);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyFreezing(TargetHelper.Enemies(),magicNumber);
    }
}
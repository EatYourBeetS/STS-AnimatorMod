package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Overheat extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Overheat.class)
            .SetStatus(0, CardRarity.COMMON, EYBCardTarget.None);

    public Overheat()
    {
        super(DATA, false);

        Initialize(0, 0, 3);

        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Draw(2);
            GameActions.Bottom.StackPower(new DelayedDamagePower(p, magicNumber));
            GameActions.Bottom.ModifyAllCopies(cardID, c -> GameUtilities.IncreaseMagicNumber(c, 1, false));
        }
    }
}
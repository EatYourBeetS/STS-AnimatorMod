package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.actions._legacy.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.HigakiRinne;

public class HigakiRinnePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class.getSimpleName());

    private final HigakiRinne higakiRinne;

    public HigakiRinnePower(AbstractPlayer owner, HigakiRinne higakiRinne, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.higakiRinne = higakiRinne;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        for (int i = 0; i < this.amount; i++)
        {
            GameActionsHelper_Legacy.AddToBottom(new HigakiRinneAction(higakiRinne));
        }

        this.flash();
    }
}

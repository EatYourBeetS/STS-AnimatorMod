package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.HigakiRinneAction;
import eatyourbeets.actions.HiteiAction;
import eatyourbeets.cards.animator.HigakiRinne;
import eatyourbeets.rewards.SpecialGoldReward;

public class HigakiRinnePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HigakiRinnePower.class.getSimpleName());

    private HigakiRinne higakiRinne;

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
            GameActionsHelper.AddToBottom(new HigakiRinneAction(higakiRinne));
        }

        this.flash();
    }
}

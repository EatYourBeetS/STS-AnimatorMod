package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;

public class FeridBathoryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(FeridBathoryPower.class.getSimpleName());

    public FeridBathoryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        super.onExhaust(card);

        GameActionsHelper.DamageRandomEnemy(owner, amount, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.GainTemporaryHP(owner, owner, amount);

        this.flash();
    }
}
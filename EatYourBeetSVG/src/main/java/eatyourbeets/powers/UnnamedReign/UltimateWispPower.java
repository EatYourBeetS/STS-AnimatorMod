package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;

public class UltimateWispPower extends AnimatorPower
{
    private boolean shouldExhaust = false;

    public static final String POWER_ID = CreateFullID(UltimateWispPower.class.getSimpleName());

    public UltimateWispPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = -1;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        shouldExhaust = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        shouldExhaust = true;
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if (shouldExhaust)
        {
            action.actionType = AbstractGameAction.ActionType.EXHAUST;
            action.exhaustCard = true;
            shouldExhaust = false;
        }
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if (target != owner && damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS)
        {
            GameActionsHelper.MakeCardInDiscardPile(new Burn(), 1, true);
        }
    }
}
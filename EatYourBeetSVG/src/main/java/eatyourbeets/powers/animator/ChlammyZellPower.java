package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZellPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ChlammyZellPower.class.getSimpleName());

    private AbstractCard.CardType lastType;

    public ChlammyZellPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        lastType = AbstractCard.CardType.SKILL;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + lastType;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        LosePower();

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        if (usedCard.type != lastType)
        {
            lastType = usedCard.type;

            int[] damage = DamageInfo.createDamageMatrix(amount, true);

            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            GameActions.Bottom.Cycle(1, name);

            updateDescription();
        }
    }
}

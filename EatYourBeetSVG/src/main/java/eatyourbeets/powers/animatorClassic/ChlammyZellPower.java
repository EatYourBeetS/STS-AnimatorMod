package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class ChlammyZellPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(ChlammyZellPower.class);

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
        RemovePower();

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
            GameActions.Bottom.Cycle(name, 1);

            updateDescription();
        }
    }
}

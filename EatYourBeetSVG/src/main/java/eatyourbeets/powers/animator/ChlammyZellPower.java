package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

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
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));

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
            GameActionsHelper_Legacy.DamageAllEnemies(owner, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            GameActionsHelper_Legacy.CycleCardAction(1, name);
            updateDescription();
        }
    }
}

package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class StrategicInformationPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(StrategicInformationPower.class.getSimpleName());

    private static final int BLOCK_AMOUNT = 2;
    private static final int DAMAGE_AMOUNT = 2;

    private int lastDiscardCount;
    private int uses;

    public StrategicInformationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.uses = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] +
        BLOCK_AMOUNT + powerStrings.DESCRIPTIONS[2] + DAMAGE_AMOUNT + powerStrings.DESCRIPTIONS[3];
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        this.lastDiscardCount = GameActionManager.totalDiscardedThisTurn;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.uses += stackAmount;
    }

    @Override
    public void onDrawOrDiscard()
    {
        super.onDrawOrDiscard();

        int discarded = GameActionManager.totalDiscardedThisTurn;
        if (amount > 0 && lastDiscardCount < discarded)
        {
            ArrayList<AbstractMonster> enemies = GameUtilities.GetCurrentEnemies(true);

            AbstractMonster target = null;
            int minHealth = Integer.MAX_VALUE;
            for (AbstractMonster m : enemies)
            {
                if (m.currentHealth < minHealth)
                {
                    minHealth = m.currentHealth;
                    target = m;
                }
            }

            while (amount > 0 && lastDiscardCount < discarded)
            {
                GameActionsHelper.GainBlock(owner, BLOCK_AMOUNT);

                if (target != null)
                {
                    GameActionsHelper.DamageTarget(owner, target, DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH);
                }

                amount -= 1;
                lastDiscardCount += 1;
            }

            this.flash();
            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        lastDiscardCount = 0;
        amount = uses;
    }
}
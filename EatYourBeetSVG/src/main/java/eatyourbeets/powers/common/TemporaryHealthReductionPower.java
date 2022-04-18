package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class TemporaryHealthReductionPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TemporaryHealthReductionPower.class);

    protected int initialHealth;
    protected AbstractGameAction.AttackEffect attackEffect;

    public TemporaryHealthReductionPower(AbstractCreature owner, int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(owner, POWER_ID);

        this.attackEffect = attackEffect;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, initialHealth);
    }

    @Override
    public void onInitialApplication()
    {
        initialHealth = Math.max(initialHealth, owner.currentHealth);

        super.onInitialApplication();
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        initialHealth = Math.max(initialHealth, owner.currentHealth);

        if (difference > 0)
        {
            GameActions.Top.LoseHP(owner, owner, difference, attackEffect)
            .IgnoreTempHP(true)
            .CanKill(false);
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        if (enabled && owner.currentHealth < initialHealth)
        {
            owner.heal(initialHealth - owner.currentHealth);
            SetEnabled(false);
        }
    }

    @Override
    protected ColoredString GetPrimaryAmount(Color c)
    {
        return initialHealth > 0 ? new ColoredString(initialHealth, Color.GREEN, c.a) : null;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryHealthReductionPower(owner, amount, attackEffect);
    }
}

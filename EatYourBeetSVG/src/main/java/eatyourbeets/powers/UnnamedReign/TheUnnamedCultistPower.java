package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.EnchantedArmorPower;
import eatyourbeets.powers.PlayerStatistics;

public class TheUnnamedCultistPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedCultistPower.class.getSimpleName());

    public TheUnnamedCultistPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, EnchantedArmorPower.POWER_ID));
        int count = PlayerStatistics.GetCurrentEnemies(true).size() - 1;
        if (count > 0)
        {
            GameActionsHelper.ApplyPowerSilently(owner, owner, new EnchantedArmorPower(owner, amount * count), amount * count);
        }
    }
}

package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.EnchantedArmorPower;
import eatyourbeets.powers.PlayerStatistics;

public class DarkCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarkCrystalPower.class.getSimpleName());

    public DarkCrystalPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner)
        {
            this.flash();
            AbstractCreature target = PlayerStatistics.GetRandomCharacter(true);
            GameActionsHelper.ApplyPower(null, target, new EnchantedArmorPower(target, amount), amount);
        }

        return damageAmount;
    }
}

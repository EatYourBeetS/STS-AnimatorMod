package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.JUtils;

public class EnchantmentPower extends AnimatorClickablePower
{
    public final Enchantment enchantment;

    protected String enchantmentDescription;

    public EnchantmentPower(EnchantableRelic relic, AbstractCreature owner, int amount)
    {
        super(owner, relic, PowerTriggerConditionType.Special, relic.enchantment1.GetPowerCost(), relic.enchantment1::CanUsePower, relic.enchantment1::PayPowerCost);

        this.enchantment = relic.enchantment1;
        this.ID += "(" + enchantment.index + "-" + enchantment.auxiliaryData.form + ")";
        this.triggerCondition.requiresTarget = enchantment.requiresTarget;
        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        if (enchantmentDescription == null)
        {
            enchantment.cardText.ForceRefresh();
            enchantmentDescription = enchantment.rawDescription
            .replace("!S!", String.valueOf(enchantment.secondaryValue))
            .replace("!M!", String.valueOf(enchantment.magicNumber))
            .replace(" NL ", " ")
            .replace("{", "")
            .replace("~", "")
            .replace("}", "");
            enchantmentDescription = JUtils.ModifyString(enchantmentDescription, w -> Character.isDigit(w.charAt(0)) ? ("#b" + w) : w);
        }

        return enchantmentDescription;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        this.enchantment.AtEndOfTurnEffect(isPlayer);
    }
}
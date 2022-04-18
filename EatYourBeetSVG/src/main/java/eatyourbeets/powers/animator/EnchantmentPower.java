package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class EnchantmentPower extends AnimatorClickablePower
{
    public final Enchantment enchantment;

    protected String enchantmentDescription;

    public EnchantmentPower(EnchantableRelic relic, AbstractCreature owner, int amount)
    {
        super(owner, relic, PowerTriggerConditionType.Special, relic.enchantment.GetPowerCost(), relic.enchantment::CanUsePower, relic.enchantment::PayPowerCost);

        this.enchantment = relic.enchantment;
        this.ID += "(" + enchantment.index + "-" + enchantment.upgradeIndex + ")";
        this.triggerCondition.requiresTarget = enchantment.requiresTarget;
        this.enchantment.SetUses(triggerCondition);
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
    public void OnUse(AbstractMonster m)
    {
        super.OnUse(m);

        this.enchantment.UsePower(m);

        if (triggerCondition.uses == 0 && !triggerCondition.refreshEachTurn)
        {
            GameActions.Bottom.WaitRealtime(0.2f);
            RemovePower(GameActions.Bottom);
        }
    }
}
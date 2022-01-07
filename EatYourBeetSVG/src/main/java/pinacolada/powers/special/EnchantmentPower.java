package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.powers.PCLClickablePower;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.utilities.PCLJUtils;

public class EnchantmentPower extends PCLClickablePower
{
    public final Enchantment enchantment;

    protected String enchantmentDescription;

    public EnchantmentPower(PCLEnchantableRelic relic, AbstractCreature owner, int amount)
    {
        super(owner, relic, relic.enchantment.triggerConditionType, relic.enchantment.GetPowerCost(), null, null, relic.enchantment.GetAffinityList());

        this.enchantment = relic.enchantment;
        this.ID += "(" + enchantment.index + "-" + enchantment.auxiliaryData.form + ")";
        this.triggerCondition.requiresTarget = enchantment.requiresTarget;
        this.triggerCondition.SetUses(enchantment.uses, enchantment.refreshEachTurn, true);
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
            enchantmentDescription = PCLJUtils.ModifyString(enchantmentDescription, w -> Character.isDigit(w.charAt(0)) ? ("#b" + w) : w);
        }

        return enchantmentDescription;
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);

        this.enchantment.UsePower(m);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        this.enchantment.AtEndOfTurnEffect(isPlayer);
    }
}
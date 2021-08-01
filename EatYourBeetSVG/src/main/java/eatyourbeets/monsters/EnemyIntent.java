package eatyourbeets.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.blights.Spear;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public class EnemyIntent
{
    private static final WeakPower WEAK = new WeakPower(null, 1, false);
    private static final VulnerablePower VULNERABLE = new VulnerablePower(null, 0, false);
    private static final StrengthPower STRENGTH = new StrengthPower(null, 0);
    private static final FreezingPower FREEZING = new FreezingPower(null, null, 0);
    private static final FieldInfo<EnemyMoveInfo> _move = JUtils.GetField("move", AbstractMonster.class);

    public final AbstractMonster enemy;
    public final AbstractMonster.Intent intent;
    public final EnemyMoveInfo move;
    public final boolean isAttacking;
    public final HashMap<String, Integer> modifiers = new HashMap<>();

    public EnemyIntent(AbstractMonster enemy)
    {
        this.enemy = enemy;
        this.intent = enemy.intent;
        this.move = _move.Get(enemy);
        this.isAttacking = move.baseDamage >= 0; // It is -1 if not attacking
    }

    public int GetDamage(boolean multi)
    {
        return isAttacking ? ((multi ? GetDamageMulti() : 1) * enemy.getIntentDmg()) : 0;
    }

    public int GetBaseDamage(boolean multi)
    {
        return isAttacking ? ((multi ? GetDamageMulti() : 1) * enemy.getIntentBaseDmg()) : 0;
    }

    public int GetDamageMulti()
    {
        return isAttacking ? (move.isMultiDamage ? move.multiplier : 1) : 0;
    }

    public EnemyIntent AddWeak()
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(enemy, this::GetIntentDamageString);
            modifiers.put(WeakPower.POWER_ID, 1);
        }

        return this;
    }

    public EnemyIntent AddPlayerVulnerable()
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(enemy, this::GetIntentDamageString);
            modifiers.put(VulnerablePower.POWER_ID, 1);
        }

        return this;
    }

    public EnemyIntent AddEnchantedArmor(int amount)
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(enemy, this::GetIntentDamageString);
            modifiers.put(EnchantedArmorPower.POWER_ID, amount);
        }

        return this;
    }

    public EnemyIntent AddStrength(int amount)
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(enemy, this::GetIntentDamageString);
            modifiers.put(StrengthPower.POWER_ID, amount);
        }

        return this;
    }

    public EnemyIntent AddFreezing()
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(enemy, this::GetIntentDamageString);
            modifiers.put(FreezingPower.POWER_ID, 1);
        }

        return this;
    }

    protected ColoredString GetIntentDamageString()
    {
        final int baseDamage = GetBaseDamage(false);
        final int damage = CalculateIntentDamage();
        final ColoredString result = new ColoredString(damage);
        if (damage > baseDamage)
        {
            result.color = Settings.RED_TEXT_COLOR.cpy().lerp(Color.WHITE, 0.2f);
        }
        else if (damage < baseDamage)
        {
            result.color = Settings.GREEN_TEXT_COLOR.cpy().lerp(Color.WHITE, 0.2f);
        }

        return result;
    }

    protected int CalculateIntentDamage()
    {
        final AbstractPlayer player = AbstractDungeon.player;
        float damage = GetBaseDamage(false);
        int weak = modifiers.getOrDefault(WeakPower.POWER_ID, 0);
        int freezing = modifiers.getOrDefault(FreezingPower.POWER_ID, 0);
        int vulnerable = modifiers.getOrDefault(VulnerablePower.POWER_ID, 0);
        int strength = modifiers.getOrDefault(StrengthPower.POWER_ID, 0);
        int enchantedArmor = modifiers.getOrDefault(EnchantedArmorPower.POWER_ID, 0);

        if (player.hasBlight(Spear.ID))
        {
            damage *= player.getBlight(Spear.ID).effectFloat();
        }

        if (strength != 0)
        {
            STRENGTH.owner = enemy;
            STRENGTH.amount = strength;
            damage = STRENGTH.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : enemy.powers)
        {
            if (p.ID.equals(WeakPower.POWER_ID))
            {
                weak = 0;
            }
            else if (p.ID.equals(FreezingPower.POWER_ID))
            {
                freezing = 0;
            }

            damage = p.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        if (freezing != 0)
        {
            FREEZING.owner = enemy;
            damage = FREEZING.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        if (weak != 0)
        {
            WEAK.owner = enemy;
            damage = WEAK.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : player.powers)
        {
            if (vulnerable == 0 && VulnerablePower.POWER_ID.equals(p.ID))
            {
                vulnerable = 1;
            }
            else if (enchantedArmor != 0 && EnchantedArmorPower.POWER_ID.equals(p.ID))
            {
                damage *= EnchantedArmorPower.CalculatePercentage(p.amount + enchantedArmor);
                enchantedArmor = 0;
            }
            else
            {
                damage = p.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
            }
        }

        if (vulnerable != 0)
        {
            VULNERABLE.owner = player;
            damage = VULNERABLE.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        if (enchantedArmor != 0)
        {
            damage *= EnchantedArmorPower.CalculatePercentage(enchantedArmor);
        }

        damage = player.stance.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);

        //if (monster.applyBackAttack())
        if (player.hasPower(SurroundedPower.POWER_ID) && (player.flipHorizontal && player.drawX < enemy.drawX || !player.flipHorizontal && player.drawX > enemy.drawX))
        {
            damage = (float) ((int) (damage * 1.5f));
        }

        for (AbstractPower p : enemy.powers)
        {
            damage = p.atDamageFinalGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : player.powers)
        {
            damage = p.atDamageFinalReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        return Math.max(0, MathUtils.floor(damage));
    }
}

package eatyourbeets.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.blights.Spear;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EnemyIntent
{
    private static class TemporaryPowers
    {
        private static final ArrayList<AbstractPower> DEFAULT_PLAYER_POWERS = new ArrayList<>();
        private static final VulnerablePower VULNERABLE = new VulnerablePower(null, 0, false);
        private static final EnchantedArmorPower ENCHANTED_ARMOR = new EnchantedArmorPower(null, 0, false);
        private static final BurningPower BURNING = new BurningPower(null, null, 0);

        private static final ArrayList<AbstractPower> DEFAULT_ENEMY_POWERS = new ArrayList<>();
        private static final WeakPower WEAK = new WeakPower(null, 1, false);
        private static final StrengthPower STRENGTH = new StrengthPower(null, 0);
        private static final FreezingPower FREEZING = new FreezingPower(null, null, 0);

        private static final ArrayList<AbstractPower> PLAYER_POWERS = new ArrayList<>();
        private static final ArrayList<AbstractPower> ENEMY_POWERS = new ArrayList<>();

        static
        {
            DEFAULT_PLAYER_POWERS.add(VULNERABLE);
            DEFAULT_PLAYER_POWERS.add(ENCHANTED_ARMOR);
            DEFAULT_PLAYER_POWERS.add(BURNING);
            DEFAULT_ENEMY_POWERS.add(WEAK);
            DEFAULT_ENEMY_POWERS.add(STRENGTH);
            DEFAULT_ENEMY_POWERS.add(FREEZING);
        }

        protected static void Load(AbstractPlayer player, AbstractMonster enemy, HashMap<String, Integer> modifiers)
        {
            PLAYER_POWERS.clear();
            PLAYER_POWERS.addAll(player.powers);
            LoadPowers(player, DEFAULT_PLAYER_POWERS, PLAYER_POWERS, modifiers);

            ENEMY_POWERS.clear();
            ENEMY_POWERS.addAll(enemy.powers);
            LoadPowers(enemy, DEFAULT_ENEMY_POWERS, ENEMY_POWERS, modifiers);
        }

        protected static void LoadPowers(AbstractCreature c, ArrayList<AbstractPower> defaultPowers, ArrayList<AbstractPower> powers, HashMap<String, Integer> modifiers)
        {
            boolean sort = false;
            for (AbstractPower p : defaultPowers)
            {
                if (LoadPower(c, powers, modifiers, p))
                {
                    sort = true;
                }
            }

            if (sort)
            {
                Collections.sort(powers);
            }
        }

        protected static boolean LoadPower(AbstractCreature owner, ArrayList<AbstractPower> powers, HashMap<String, Integer> modifiers, AbstractPower power)
        {
            if ((power.amount = modifiers.getOrDefault(power.ID, 0)) != 0)
            {
                power.owner = owner;

                for (int i = 0; i < powers.size(); i++)
                {
                    final AbstractPower p = powers.get(i);
                    if (p.ID.equals(power.ID))
                    {
                        power.amount += p.amount;
                        if (power.amount == 0)
                        {
                            powers.remove(power);
                        }
                        else
                        {
                            powers.set(i, power);
                        }

                        return true;
                    }
                }

                powers.add(power);
                return true;
            }

            return false;
        }
    }

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

        TemporaryPowers.Load(player, enemy, modifiers);

        if (player.hasBlight(Spear.ID))
        {
            damage *= player.getBlight(Spear.ID).effectFloat();
        }

        for (AbstractPower p : TemporaryPowers.ENEMY_POWERS)
        {
            damage = p.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : TemporaryPowers.PLAYER_POWERS)
        {
            damage = p.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        damage = player.stance.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);

        //if (monster.applyBackAttack())
        if (player.hasPower(SurroundedPower.POWER_ID) && (player.flipHorizontal && player.drawX < enemy.drawX || !player.flipHorizontal && player.drawX > enemy.drawX))
        {
            damage = (float) ((int) (damage * 1.5f));
        }

        for (AbstractPower p : TemporaryPowers.ENEMY_POWERS)
        {
            damage = p.atDamageFinalGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : TemporaryPowers.PLAYER_POWERS)
        {
            damage = p.atDamageFinalReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        return Math.max(0, MathUtils.floor(damage));
    }
}

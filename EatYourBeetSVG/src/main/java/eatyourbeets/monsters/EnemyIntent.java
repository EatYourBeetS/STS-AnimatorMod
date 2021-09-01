package eatyourbeets.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.blights.Spear;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.powers.common.BlindedPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import patches.abstractMonster.AbstractMonsterPatches;

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
        private static final BlindedPower BLINDED = new BlindedPower(null, null, 0);

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
            DEFAULT_ENEMY_POWERS.add(BLINDED);
        }

        protected static void Load(AbstractPlayer player, AbstractMonster enemy, HashMap<String, Integer> modifiers)
        {
            PLAYER_POWERS.clear();
            for (AbstractPower p : player.powers)
            {
                if (!(p instanceof InvisiblePower))
                {
                    PLAYER_POWERS.add(p);
                }
            }
            LoadPowers(player, DEFAULT_PLAYER_POWERS, PLAYER_POWERS, modifiers);

            ENEMY_POWERS.clear();
            for (AbstractPower p : enemy.powers)
            {
                if (!(p instanceof InvisiblePower))
                {
                    ENEMY_POWERS.add(p);
                }
            }
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
                for (AbstractPower p : powers)
                {
                    CombatStats.ApplyPowerPriority(p);
                }

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
                            powers.remove(i);
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
    public final HashMap<String, Integer> modifiers = new HashMap<>();
    public AbstractMonster.Intent intent;
    private EnemyMoveInfo move;
    private boolean isAttacking;
    private boolean isDefending;

    protected EnemyIntent(AbstractMonster enemy)
    {
        this.enemy = enemy;
    }

    public static EnemyIntent Get(AbstractMonster enemy)
    {
        EnemyIntent intent = AbstractMonsterPatches.AbstractMonster_Fields.enemyIntent.get(enemy);
        if (intent == null)
        {
            intent = new EnemyIntent(enemy);
            AbstractMonsterPatches.AbstractMonster_Fields.enemyIntent.set(enemy, intent);
        }

        intent.intent = enemy.intent;
        intent.move = _move.Get(enemy);
        intent.isAttacking = intent.move.baseDamage >= 0; // It is -1 if not attacking

        return intent;
    }

    public boolean IsAttacking()
    {
        return isAttacking;
    }

    public boolean IsBuffing()
    {
        return !(intent.equals(AbstractMonster.Intent.BUFF) || intent.equals(AbstractMonster.Intent.ATTACK_BUFF) || intent.equals(AbstractMonster.Intent.DEFEND_BUFF));
    }

    public boolean IsDebuffing()
    {
        return !(intent.equals(AbstractMonster.Intent.DEBUFF) || intent.equals(AbstractMonster.Intent.ATTACK_DEBUFF) || intent.equals(AbstractMonster.Intent.DEFEND_DEBUFF) || intent.equals(AbstractMonster.Intent.STRONG_DEBUFF));
    }

    public boolean IsDefending()
    {
        return !(intent.equals(AbstractMonster.Intent.DEFEND) || intent.equals(AbstractMonster.Intent.DEFEND_BUFF) || intent.equals(AbstractMonster.Intent.DEFEND_DEBUFF) || intent.equals(AbstractMonster.Intent.ATTACK_DEFEND));
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
        return AddModifier(enemy, WeakPower.POWER_ID, 1);
    }

    public EnemyIntent AddPlayerVulnerable()
    {
        return AddModifier(enemy, VulnerablePower.POWER_ID, 1);
    }

    public EnemyIntent AddEnchantedArmor(int amount)
    {
        return AddModifier(enemy, EnchantedArmorPower.POWER_ID, amount);
    }

    public EnemyIntent AddStrength(int amount)
    {
        return AddModifier(enemy, StrengthPower.POWER_ID, amount);
    }

    public EnemyIntent AddFreezing()
    {
        return AddModifier(enemy, FreezingPower.POWER_ID, 1);
    }

    public EnemyIntent AddBlinded()
    {
        return AddModifier(enemy, BlindedPower.POWER_ID, 1);
    }

    public EnemyIntent AddModifier(AbstractMonster enemy, String powerID, int amount)
    {
        if (isAttacking)
        {
            GR.UI.CombatScreen.Intents.Add(this);
            modifiers.put(powerID, amount);
        }

        return this;
    }

    public ColoredString CreateIntentDamageString()
    {
        final int currentDamage = GetDamage(false);
        final int damage = ModifyIntentDamage(GetBaseDamage(false));
        final ColoredString result = new ColoredString(damage);
        if (damage > currentDamage)
        {
            result.color = Settings.RED_TEXT_COLOR.cpy().lerp(Color.WHITE, 0.2f);
        }
        else if (damage < currentDamage)
        {
            result.color = Settings.GREEN_TEXT_COLOR.cpy().lerp(Color.WHITE, 0.2f);
        }

        return result;
    }

    protected int ModifyIntentDamage(float damage)
    {
        final AbstractPlayer player = AbstractDungeon.player;

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

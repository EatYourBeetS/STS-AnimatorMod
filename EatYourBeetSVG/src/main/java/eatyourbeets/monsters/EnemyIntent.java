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
import eatyourbeets.powers.animator.EnchantedArmorPlayerPower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.powers.replacement.AnimatorIntangiblePower;
import eatyourbeets.powers.unnamed.WitheringPower;
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
    protected static class PlayerPowers
    {
        protected static final VulnerablePower VULNERABLE = new VulnerablePower(null, 0, false);
        protected static final EnchantedArmorPlayerPower ENCHANTED_ARMOR = new EnchantedArmorPlayerPower(null, 0);
        protected static final AnimatorIntangiblePower INTANGIBLE = new AnimatorIntangiblePower(null, 0);
        protected static final BurningPower BURNING = new BurningPower(null, null, 0);

        protected static final ArrayList<AbstractPower> POWERS = new ArrayList<>();
        static
        {
            POWERS.add(VULNERABLE);
            POWERS.add(ENCHANTED_ARMOR);
            POWERS.add(INTANGIBLE);
            POWERS.add(BURNING);
        }
    }

    protected static class EnemyPowers
    {
        protected static final WeakPower WEAK = new WeakPower(null, 1, false);
        protected static final StrengthPower STRENGTH = new StrengthPower(null, 0);
        protected static final FreezingPower FREEZING = new FreezingPower(null, null, 0);
        protected static final WitheringPower WITHERING = new WitheringPower(null, 0);
        protected static final ArrayList<AbstractPower> POWERS = new ArrayList<>();
        static
        {
            POWERS.add(WEAK);
            POWERS.add(STRENGTH);
            POWERS.add(FREEZING);
            POWERS.add(WITHERING);
        }
    }

    protected static class TemporaryPowers
    {
        private static final ArrayList<AbstractPower> playerPowers = new ArrayList<>();
        private static final ArrayList<AbstractPower> enemyPowers = new ArrayList<>();

        protected static void Load(AbstractPlayer player, AbstractMonster enemy, HashMap<String, Integer> modifiers)
        {
            playerPowers.clear();
            for (AbstractPower p : player.powers)
            {
                if (!(p instanceof InvisiblePower))
                {
                    playerPowers.add(p);
                }
            }
            LoadPowers(player, PlayerPowers.POWERS, playerPowers, modifiers);

            enemyPowers.clear();
            for (AbstractPower p : enemy.powers)
            {
                if (!(p instanceof InvisiblePower))
                {
                    enemyPowers.add(p);
                }
            }
            LoadPowers(enemy, EnemyPowers.POWERS, enemyPowers, modifiers);
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

    public static AbstractMonster currentEnemy;
    public final AbstractMonster enemy;
    public final HashMap<String, Integer> modifiers = new HashMap<>();
    public AbstractMonster.Intent intent;
    public EnemyMoveInfo move;
    public boolean isAttacking;

    protected EnemyIntent(AbstractMonster enemy)
    {
        this.enemy = enemy;
    }

    public static void CreatePlayerPower(AbstractPower power, boolean replaceExisting)
    {
        final ArrayList<AbstractPower> powers = PlayerPowers.POWERS;
        for (int i = 0; i < powers.size(); i++)
        {
            if (powers.get(i).ID.equals(power.ID))
            {
                if (replaceExisting)
                {
                    powers.set(i, power);
                }

                return;
            }
        }

        powers.add(power);
    }

    public static EnemyIntent GetCurrentIntent()
    {
        return currentEnemy == null ? null : Get(currentEnemy);
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
        intent.isAttacking = (intent.move != null && intent.move.baseDamage >= 0); // It is -1 if not attacking

        return intent;
    }

    public boolean IsAttacking()
    {
        return isAttacking;
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

    public EnemyIntent AddPlayerIntangible()
    {
        return AddModifier(PlayerPowers.INTANGIBLE.ID, 1);
    }

    public EnemyIntent AddPlayerVulnerable()
    {
        return AddModifier(PlayerPowers.VULNERABLE.ID, 1);
    }

    public EnemyIntent AddPlayerBurning()
    {
        return AddModifier(PlayerPowers.BURNING.ID, 1);
    }

    public EnemyIntent AddPlayerEnchantedArmor(int amount)
    {
        return AddModifier(PlayerPowers.ENCHANTED_ARMOR.ID, amount);
    }

    public EnemyIntent AddWeak()
    {
        return AddModifier(EnemyPowers.WEAK.ID, 1);
    }

    public EnemyIntent AddStrength(int amount)
    {
        return AddModifier(EnemyPowers.STRENGTH.ID, amount);
    }

    public EnemyIntent AddFreezing()
    {
        return AddModifier(EnemyPowers.FREEZING.ID, 1);
    }

    public EnemyIntent AddWithering(int amount)
    {
        return AddModifier(EnemyPowers.WITHERING.ID, amount);
    }

    public EnemyIntent AddModifier(String powerID, int amount)
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
        currentEnemy = enemy;

        final AbstractPlayer player = AbstractDungeon.player;
        TemporaryPowers.Load(player, enemy, modifiers);

        if (player.hasBlight(Spear.ID))
        {
            damage *= player.getBlight(Spear.ID).effectFloat();
        }

        for (AbstractPower p : TemporaryPowers.enemyPowers)
        {
            damage = p.atDamageGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : TemporaryPowers.playerPowers)
        {
            damage = p.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        damage = player.stance.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);

        //if (monster.applyBackAttack())
        if (player.hasPower(SurroundedPower.POWER_ID) && (player.flipHorizontal && player.drawX < enemy.drawX || !player.flipHorizontal && player.drawX > enemy.drawX))
        {
            damage = (float) ((int) (damage * 1.5f));
        }

        for (AbstractPower p : TemporaryPowers.enemyPowers)
        {
            damage = p.atDamageFinalGive(damage, DamageInfo.DamageType.NORMAL);
        }

        for (AbstractPower p : TemporaryPowers.playerPowers)
        {
            damage = p.atDamageFinalReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        currentEnemy = null;
        return Math.max(0, MathUtils.floor(damage));
    }
}

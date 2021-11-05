package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.animator.ElementalExposurePower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;
import java.util.UUID;

public class ElectrifiedPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = new Color(1.0F, 0.9F, 0.25F, 1.0F);
    public static final String POWER_ID = CreateFullID(ElectrifiedPower.class);
    public static final int SPLASH_MULTIPLIER = 15;
    public static int PLAYER_SPLASH_BONUS = 0;
    private static UUID battleID;

    public static void AddPlayerReductionBonus(int multiplier)
    {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_SPLASH_BONUS = 0;
        }

        if (multiplier > 0)
        {
            PLAYER_SPLASH_BONUS += multiplier;
            GameUtilities.UpdatePowerDescriptions();
        }
    }

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Mathf.Max(1, damage * (multiplier / 100f));
    }

    public ElectrifiedPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetPassiveDamage(), GetMultiplier());
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ORB_LIGHTNING_PASSIVE, 0.95f, 1.05f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        Trigger();
        ReducePower(1);
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
            if (GameUtilities.IsPlayer(owner) || enemies.size() == 1) {
                GameActions.Bottom.DealDamage(owner, owner, (int) CalculateDamage(damageAmount, GetMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
            }
            else {
                for (AbstractMonster enemy : enemies) {
                    if (enemy != owner) {
                        GameActions.Bottom.DealDamage(owner, enemy, (int) CalculateDamage(damageAmount, GetMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
                    }
                }
            }
            this.flash();
        }
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, GetPassiveDamage(), false, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    public int GetPassiveDamage()
    {
        return MathUtils.round((amount == 1 ? 1 : amount < 1 ? 0 : amount / 2 + amount % 2) * ElementalExposurePower.CalculatePercentage(owner));
    }

    public float GetMultiplier() {
        return (GameUtilities.IsPlayer(owner)) ? SPLASH_MULTIPLIER : (SPLASH_MULTIPLIER + PLAYER_SPLASH_BONUS) * ElementalExposurePower.CalculatePercentage(owner);
    }

    public void Trigger() {
        GameActions.Bottom.LoseHP(source, owner, GetPassiveDamage(), AttackEffects.SPARK)
                .CanKill(owner == null || !owner.isPlayer);
    }
}

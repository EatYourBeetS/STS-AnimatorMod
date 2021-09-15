package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.animator.ElementalExposurePower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class BurningPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.ORANGE.cpy();
    private static UUID battleID;

    public static final String POWER_ID = CreateFullID(BurningPower.class);
    public static final int ATTACK_MULTIPLIER = 15;
    public static int PLAYER_ATTACK_BONUS = 0;

    public static void AddPlayerAttackBonus(int multiplier)
    {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_ATTACK_BONUS = 0;
        }

        if (multiplier > 0)
        {
            PLAYER_ATTACK_BONUS += multiplier;

            for (BurningPower p : GameUtilities.<BurningPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
            {
                p.updateDescription();
                p.flashWithoutSound();
            }
        }
    }

    public static float CalculateDamage(float damage, float multiplier)
    {
        return damage + Mathf.Max(1, damage * (multiplier / 100f));
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount)
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
    public void onInitialApplication()
    {
        super.onInitialApplication();

        AddPlayerAttackBonus(0); // Refresh multiplier
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_FIRE, 0.95f, 1.05f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        GameActions.Bottom.LoseHP(source, owner, GetPassiveDamage(), AttackEffects.FIRE)
        .CanKill(owner == null || !owner.isPlayer);
        ReducePower(1);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageReceive(type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, GetMultiplier()) : damage, type);
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
        return MathUtils.round((amount == 1 ? 1 : amount < 1 ? 0 : amount / 2 + amount % 2) * GetElementalExposure());
    }

    private float GetMultiplier()
    {
        return (GameUtilities.IsPlayer(owner)) ? ATTACK_MULTIPLIER : (ATTACK_MULTIPLIER + PLAYER_ATTACK_BONUS) * GetElementalExposure();
    }

    private float GetElementalExposure() {
        return ElementalExposurePower.CalculatePercentage(GameUtilities.GetPowerAmount(owner, ElementalExposurePower.POWER_ID));
    }
}
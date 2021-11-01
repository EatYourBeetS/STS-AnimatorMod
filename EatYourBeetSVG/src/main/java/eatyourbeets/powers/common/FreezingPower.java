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

import java.util.UUID;

public class FreezingPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = new Color(0.372F, 0.5F, 1.0F, 1.0F);
    public static final String POWER_ID = CreateFullID(FreezingPower.class);
    public static final int REDUCTION_MULTIPLIER = 10;
    public static final int MAX_REDUCTION_MULTIPLIER = 50;
    public static int PLAYER_REDUCTION_BONUS = 0;
    private static UUID battleID;

    public static void AddPlayerReductionBonus(int multiplier)
    {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_REDUCTION_BONUS = 0;
        }

        if (multiplier > 0)
        {
            PLAYER_REDUCTION_BONUS += multiplier;
            GameUtilities.UpdatePowerDescriptions();
        }
    }

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Math.max(0, damage - Math.max(1f, damage * (multiplier / 100f)));
    }

    public FreezingPower(AbstractCreature owner, AbstractCreature source, int amount)
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
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.95f, 1.05f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        Trigger();
        ReducePower(1);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive((type == DamageInfo.DamageType.NORMAL) ? CalculateDamage(damage, GetMultiplier()) : damage, type);
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

    public float GetMultiplier() {
        return (GameUtilities.IsPlayer(owner)) ? REDUCTION_MULTIPLIER : Math.min(MAX_REDUCTION_MULTIPLIER, REDUCTION_MULTIPLIER + PLAYER_REDUCTION_BONUS) * GetElementalExposure();
    }

    private float GetElementalExposure() {
        return ElementalExposurePower.CalculatePercentage(GameUtilities.GetPowerAmount(owner, ElementalExposurePower.POWER_ID));
    }

    public void Trigger() {
        GameActions.Bottom.LoseHP(source, owner, GetPassiveDamage(), AttackEffects.ICE)
                .CanKill(owner == null || !owner.isPlayer);
    }
}

package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.PaperCrane;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.UUID;

public class AnimatorWeakPower extends WeakPower implements CloneablePowerInterface
{
    private boolean justApplied = false;
    private static UUID battleID;
    public static final int ATTACK_MULTIPLIER = 25;
    public static int PLAYER_MODIFIER = 0;
    public static int ENEMY_MODIFIER = 0;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Math.max(0, damage - Math.max(0, damage * (multiplier / 100f)));
    }

    public static void AddPlayerModifier(int multiplier)
    {
        InitializeForBattle();
        PLAYER_MODIFIER += multiplier;

        for (WeakPower p : GameUtilities.<WeakPower>GetPowers(TargetHelper.Player(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
    }

    public static void AddEnemyModifier(int multiplier)
    {
        InitializeForBattle();
        ENEMY_MODIFIER += multiplier;

        for (WeakPower p : GameUtilities.<WeakPower>GetPowers(TargetHelper.Enemies(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
    }

    private static void InitializeForBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_MODIFIER = 0;
            ENEMY_MODIFIER = GameUtilities.HasRelicEffect(PaperCrane.ID) ? 15 : 0;
        }
    }

    public AnimatorWeakPower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner,amount,isSourceMonster);
        if (!GameUtilities.IsPlayerTurn() && isSourceMonster) {
            this.justApplied = true;
        }

        InitializeForBattle();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + GetMultiplier() + DESCRIPTIONS[1] + this.amount + (this.amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, GetMultiplier()) : damage;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorWeakPower(owner, amount, justApplied);
    }

    public int GetMultiplier()
    {
        return (GameUtilities.IsPlayer(owner)) ? (ATTACK_MULTIPLIER + PLAYER_MODIFIER) : (ATTACK_MULTIPLIER + ENEMY_MODIFIER);
    }
}

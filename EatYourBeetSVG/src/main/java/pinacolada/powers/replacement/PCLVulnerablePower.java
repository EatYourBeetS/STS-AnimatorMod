package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.OddMushroom;
import com.megacrit.cardcrawl.relics.PaperFrog;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.utilities.PCLGameUtilities;

import java.util.UUID;

public class PCLVulnerablePower extends VulnerablePower implements CloneablePowerInterface
{
    private boolean justApplied = false;
    private static UUID battleID;
    public static final int ATTACK_MULTIPLIER = 50;
    public static int PLAYER_MODIFIER = 0;
    public static int ENEMY_MODIFIER = 0;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return damage + Mathf.Max(0, damage * (multiplier / 100f));
    }

    public static void AddPlayerModifier(int multiplier)
    {
        InitializeForBattle();
        PLAYER_MODIFIER += multiplier;

        for (VulnerablePower p : PCLGameUtilities.<VulnerablePower>GetPowers(TargetHelper.Player(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
    }

    public static void AddEnemyModifier(int multiplier)
    {
        InitializeForBattle();
        ENEMY_MODIFIER += multiplier;

        for (VulnerablePower p : PCLGameUtilities.<VulnerablePower>GetPowers(TargetHelper.Enemies(), POWER_ID))
        {
            p.updateDescription();
            p.flashWithoutSound();
        }
    }

    private static void InitializeForBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            PLAYER_MODIFIER = PCLGameUtilities.HasRelicEffect(OddMushroom.ID) ? -25 : 0;
            ENEMY_MODIFIER = PCLGameUtilities.HasRelicEffect(PaperFrog.ID) ? 25 : 0;
        }
    }


    public PCLVulnerablePower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner,amount,isSourceMonster);
        if (isSourceMonster) {
            this.justApplied = true;
        }

        InitializeForBattle();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + GetMultiplier() + DESCRIPTIONS[1] + this.amount + (this.amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, GetMultiplier()) : damage;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLVulnerablePower(owner, amount, justApplied);
    }

    public int GetMultiplier()
    {
        return (PCLGameUtilities.IsPlayer(owner)) ? (ATTACK_MULTIPLIER + PLAYER_MODIFIER) : (ATTACK_MULTIPLIER + ENEMY_MODIFIER);
    }
}

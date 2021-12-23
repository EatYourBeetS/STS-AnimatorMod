package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class PCLFrailPower extends FrailPower implements CloneablePowerInterface
{
    protected static final String FAKE_POWER_ID = GR.PCL.CreateID(FrailPower.class.getSimpleName());

    protected static final PowerStrings fakePowerStrings;
    private boolean justApplied = false;
    public static final int BLOCK_MULTIPLIER = 25;

    static {
        fakePowerStrings = CardCrawlGame.languagePack.getPowerStrings(FAKE_POWER_ID);
    }

    public static float CalculateBlock(float block, float multiplier)
    {
        return Math.max(0, block - Math.max(0, block * (multiplier / 100f)));
    }

    public PCLFrailPower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner,amount,isSourceMonster);
        if (isSourceMonster) {
            this.justApplied = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = PCLJUtils.Format(fakePowerStrings.DESCRIPTIONS[0],GetMultiplier(),amount,amount == 1 ? fakePowerStrings.DESCRIPTIONS[1] : fakePowerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return CalculateBlock(blockAmount, GetMultiplier());
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLFrailPower(owner, amount, justApplied);
    }

    public int GetMultiplier()
    {
        return (PCLGameUtilities.IsPlayer(owner)) ? (BLOCK_MULTIPLIER + PCLCombatStats.GetPlayerEffectBonus(ID)) : (BLOCK_MULTIPLIER + PCLCombatStats.GetEffectBonus(ID));
    }
}

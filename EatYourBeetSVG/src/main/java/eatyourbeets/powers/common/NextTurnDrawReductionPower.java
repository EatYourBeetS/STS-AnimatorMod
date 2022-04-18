package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CommonPower;

public class NextTurnDrawReductionPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(NextTurnDrawReductionPower.class);

    public NextTurnDrawReductionPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.loadRegion("lessdraw");
        this.powerIcon = this.region48;

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        AbstractDungeon.player.gameHandSize -= difference;
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        RemovePower();
    }
}

package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class AlchemistGlove extends AnimatorRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class.getSimpleName());

    private static final int TURN_COUNT = 3;
    private static final int BURNING_AMOUNT = 4;

    public AlchemistGlove()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + TURN_COUNT + DESCRIPTIONS[1] + BURNING_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = 0;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = -1;
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        this.counter += 1;
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (this.counter >= TURN_COUNT)
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, BURNING_AMOUNT), BURNING_AMOUNT);
            }

            this.flash();
            this.counter = 0;
        }
    }
}
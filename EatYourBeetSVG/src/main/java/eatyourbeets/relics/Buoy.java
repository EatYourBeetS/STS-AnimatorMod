package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class Buoy extends AnimatorRelic
{
    public static final String ID = CreateFullID(Buoy.class.getSimpleName());

    private static final int PLAYER_BLOCK = 6;
    private static final int ENEMY_BLOCK = 2;

    public Buoy()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
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

        this.counter = 0;
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        counter += 1;
        if (counter == 4)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, PLAYER_BLOCK);
            for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.GainBlock(m, ENEMY_BLOCK);
            }

            this.flash();
            counter = 0;
        }
    }
}
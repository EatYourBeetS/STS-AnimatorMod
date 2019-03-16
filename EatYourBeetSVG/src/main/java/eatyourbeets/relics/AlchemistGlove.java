package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class AlchemistGlove extends AnimatorRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class.getSimpleName());

    public AlchemistGlove()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
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

        if (this.counter >= 3)
        {
            ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
            if (enemies != null && enemies.size() > 0)
            {
                AbstractPlayer p = AbstractDungeon.player;
                AbstractMonster m = Utilities.GetRandomElement(enemies, AbstractDungeon.miscRng);
                if (m != null)
                {
                    GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, 2), 2);
                }
            }

            this.flash();
            this.counter = 0;
        }
    }
}
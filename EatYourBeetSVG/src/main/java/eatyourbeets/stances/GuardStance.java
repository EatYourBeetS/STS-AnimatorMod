package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GuardStance extends EYBStance
{
    private static final int BLOCK_GAIN_ENTER = 10;
    private static final int BLOCK_GAIN_PLAYER = 10;
    private static final int BLOCK_GAIN_ENEMY = 15;

    public static final String STANCE_ID = CreateFullID(GuardStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public GuardStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Top.GainBlock(BLOCK_GAIN_ENTER);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            GameActions.Top.Add(new GainBlockAction(enemy, AbstractDungeon.player, BLOCK_GAIN_ENTER, true));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();

        GameActions.Top.GainBlock(BLOCK_GAIN_PLAYER);

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            GameActions.Top.Add(new GainBlockAction(enemy, AbstractDungeon.player, BLOCK_GAIN_ENEMY, true));
        }
    }
}

package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class OldCoffin extends AnimatorRelic
{
    public static final String ID = CreateFullID(OldCoffin.class);

    private static final int ACTIVATION_THRESHOLD = 4;

    public OldCoffin()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(false);
        this.counter = 0;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
        this.counter = -1;
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        setCounter(counter + 1);

        if (SetEnabled(counter > ACTIVATION_THRESHOLD))
        {
            AbstractMonster m = JavaUtilities.GetRandomElement(GameUtilities.GetAllEnemies(true));
            if (m != null)
            {
                GameActions.Top.Add(new RelicAboveCreatureAction(m, this));

                int n = rng.random(12);
                if (n < 4)
                {
                    GameActions.Bottom.ApplyWeak(player, m, 1);
                }
                else if (n < 8)
                {
                    GameActions.Bottom.ApplyVulnerable(player, m, 1);
                }
                else if (n <= 10)
                {
                    GameActions.Bottom.ApplyPoison(player, m, 3);
                }
                else if (n <= 11)
                {
                    GameActions.Bottom.ApplyBurning(player, m, 3);
                }
                else
                {
                    GameActions.Bottom.ApplyConstricted(player, m, 2);
                }
            }
        }
    }
}
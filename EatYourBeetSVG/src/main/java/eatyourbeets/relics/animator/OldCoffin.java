package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class OldCoffin extends AnimatorRelic
{
    public static final String ID = CreateFullID(OldCoffin.class.getSimpleName());

    private static final int ACTIVATION_THRESHOLD = 4;

    public OldCoffin()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY);
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

        counter += 1;
        if (counter > ACTIVATION_THRESHOLD)
        {
            AbstractMonster m = JavaUtilities.GetRandomElement(GameUtilities.GetCurrentEnemies(true));
            if (m != null)
            {
                GameActions.Top.Add(new RelicAboveCreatureAction(m, this));

                AbstractPlayer p = AbstractDungeon.player;
                int n = AbstractDungeon.cardRandomRng.random(12);
                if (n < 4)
                {
                    GameActions.Bottom.ApplyWeak(p, m, 1);
                }
                else if (n < 8)
                {
                    GameActions.Bottom.ApplyVulnerable(p, m, 1);
                }
                else if (n <= 10)
                {
                    GameActions.Bottom.ApplyPoison(p, m, 3);
                }
                else if (n <= 11)
                {
                    GameActions.Bottom.ApplyBurning(p, m, 3);
                }
                else
                {
                    GameActions.Bottom.ApplyConstricted(p, m, 2);
                }
            }
        }
    }
}
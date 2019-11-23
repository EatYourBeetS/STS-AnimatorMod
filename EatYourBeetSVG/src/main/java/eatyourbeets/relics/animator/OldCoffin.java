package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.powers.animator.BurningPower;

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
                AbstractPlayer p = AbstractDungeon.player;

                GameActionsHelper.AddToTop(new RelicAboveCreatureAction(m, this));

                int n = AbstractDungeon.cardRandomRng.random(12);
                if (n < 4)
                {
                    GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
                }
                else if (n < 8)
                {
                    GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
                }
                else if (n <= 10)
                {
                    GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, 3), 3);
                }
                else if (n <= 11)
                {
                    GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, 3), 3);
                }
                else
                {
                    GameActionsHelper.ApplyPower(p, m, new ConstrictedPower(m, p, 2), 2);
                }
            }
        }
    }
}
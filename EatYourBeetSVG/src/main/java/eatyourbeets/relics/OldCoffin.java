package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.powers.PlayerStatistics;

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
            AbstractMonster m = Utilities.GetRandomElement(PlayerStatistics.GetCurrentEnemies(true), AbstractDungeon.miscRng);
            if (m != null)
            {
                AbstractPlayer p = AbstractDungeon.player;
                int n = AbstractDungeon.miscRng.random(11);
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
                else
                {
                    GameActionsHelper.ApplyPower(p, m, new ConstrictedPower(m, p, 2), 2);
                }
            }
        }
    }
}
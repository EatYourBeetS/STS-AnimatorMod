package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SoulGem3 extends AnimatorRelic implements OnChannelOrbSubscriber
{
    public static final String ID = CreateFullID(SoulGem3.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 3;

    public SoulGem3()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        GameActions.Bottom.GainFocus(POWER_AMOUNT);
        CombatStats.onChannelOrb.Subscribe(this);
        SetCounter(0);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();
        int oldAmount = GameUtilities.GetPowerAmount(player, FocusPower.POWER_ID);
        if (oldAmount < POWER_AMOUNT) {
            GameActions.Bottom.GainFocus(POWER_AMOUNT - oldAmount);
        }
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb) {
        AddCounter(1);
        if (counter > TRIGGER_THRESHOLD) {
            GameActions.Bottom.Add(new CreateRandomCurses(1, player.drawPile));
            SetCounter(0);
        }
        flash();
    }
}
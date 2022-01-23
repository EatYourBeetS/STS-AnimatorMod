package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import pinacolada.actions.special.CreateRandomCurses;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SoulGem3 extends PCLRelic implements OnChannelOrbSubscriber
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
        PCLCombatStats.onChannelOrb.Subscribe(this);
        SetCounter(0);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();
        int oldAmount = PCLGameUtilities.GetPowerAmount(player, FocusPower.POWER_ID);
        if (oldAmount < POWER_AMOUNT) {
            PCLActions.Bottom.GainFocus(POWER_AMOUNT - oldAmount);
        }
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb) {
        AddCounter(1);
        if (counter > TRIGGER_THRESHOLD) {
            PCLActions.Bottom.Add(new CreateRandomCurses(1, player.drawPile));
            SetCounter(0);
        }
        flash();
    }
}
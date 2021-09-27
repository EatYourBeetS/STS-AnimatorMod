package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SoulGem3 extends AnimatorRelic implements OnApplyPowerSubscriber, OnChannelOrbSubscriber
{
    public static final String ID = CreateFullID(SoulGem3.class);
    public static final int POWER_AMOUNT = 3;
    public static final int TRIGGER_THRESHOLD = 3;

    public SoulGem3()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        GameActions.Bottom.GainFocus(POWER_AMOUNT);
        CombatStats.onApplyPower.Subscribe(this);
        CombatStats.onChannelOrb.Subscribe(this);
        SetCounter(0);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        int oldAmount = GameUtilities.GetPowerAmount(target, FocusPower.POWER_ID);
        if (GameUtilities.IsPlayer(target) && oldAmount < POWER_AMOUNT) {
            GameUtilities.ApplyPowerInstantly(player, PowerHelper.Focus, -oldAmount);
            GameActions.Bottom.GainFocus(POWER_AMOUNT);
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
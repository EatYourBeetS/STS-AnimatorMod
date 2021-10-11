package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class HeavyHalberd extends AnimatorRelic implements OnApplyPowerSubscriber
{
    public static final String ID = CreateFullID(HeavyHalberd.class);
    public static final int FORCE_AMOUNT = 1;

    public HeavyHalberd()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, FORCE_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        CombatStats.onApplyPower.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        CombatStats.onApplyPower.Unsubscribe(this);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (source != null && source.isPlayer && VulnerablePower.POWER_ID.equals(power.ID))
        {
            GameActions.Bottom.RaiseFireLevel(1);
            this.flash();
        }
    }
}
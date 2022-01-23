package pinacolada.relics.pcl.replacement;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.relics.PCLReplacementRelic;
import pinacolada.utilities.PCLActions;

public class HeavyHalberd extends PCLReplacementRelic implements OnApplyPowerSubscriber
{
    public static final String ID = CreateFullID(HeavyHalberd.class);
    public static final int FORCE_AMOUNT = 1;

    public HeavyHalberd()
    {
        super(ID, eatyourbeets.relics.animator.HeavyHalberd.ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, FORCE_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        PCLCombatStats.onApplyPower.Subscribe(this);
    }

    @Override
    protected void DeactivateBattleEffect()
    {
        PCLCombatStats.onApplyPower.Unsubscribe(this);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (source != null && source.isPlayer && VulnerablePower.POWER_ID.equals(power.ID))
        {
            PCLActions.Bottom.GainMight(1);
            this.flash();
        }
    }
}
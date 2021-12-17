package pinacolada.powers.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;

public class BozesPower extends PCLPower implements OnSynergySubscriber
{
    public static final String POWER_ID = CreateFullID(BozesPower.class);

    public BozesPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onSynergy.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onSynergy.Unsubscribe(this);
    }

    @Override
    public void OnSynergy(AbstractCard card)
    {
        PCLActions.Bottom.StackPower(new SupportDamagePower(owner, amount));
        this.flash();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        RemovePower();
    }
}

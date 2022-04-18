package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.interfaces.subscribers.OnBlockGainedSubscriber;
import eatyourbeets.interfaces.subscribers.OnHealthBarUpdatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

public class AzrielPower extends AnimatorPower implements OnBlockGainedSubscriber, OnHealthBarUpdatedSubscriber
{
    public static final String POWER_ID = CreateFullID(AzrielPower.class);
    public static final int MAX_BLOCK_AND_TEMP_HP = 20;

    public AzrielPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, MAX_BLOCK_AND_TEMP_HP);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        ApplyBlockLimit();
        ApplyTempHPLimit();

        CombatStats.onHealthBarUpdated.Subscribe(this);
        CombatStats.onBlockGained.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onHealthBarUpdated.Unsubscribe(this);
        CombatStats.onBlockGained.Unsubscribe(this);
    }

    @Override
    public void OnBlockGained(AbstractCreature creature, int block)
    {
        if (creature == owner)
        {
            ApplyBlockLimit();
        }
    }

    @Override
    public void OnHealthBarUpdated(AbstractCreature creature)
    {
        if (creature == owner)
        {
            ApplyTempHPLimit();
        }
    }

    @Override
    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(p, target, source);

        if (p.type == PowerType.DEBUFF && !p.ID.equals(GainStrengthPower.POWER_ID) && source == owner && !target.hasPower(ArtifactPower.POWER_ID))
        {
            GameActions.Bottom.GainRandomAffinityPower(amount, true);
            this.flash();
        }
    }

    protected void ApplyBlockLimit()
    {
        if (owner.hasPower(PlayerFlightPower.POWER_ID))
        {
            if (owner.currentBlock > MAX_BLOCK_AND_TEMP_HP)
            {
                owner.currentBlock = MAX_BLOCK_AND_TEMP_HP;
            }
        }
    }

    protected void ApplyTempHPLimit()
    {
        if (owner.hasPower(PlayerFlightPower.POWER_ID))
        {
            final int tempHP = TempHPField.tempHp.get(owner);
            if (tempHP > MAX_BLOCK_AND_TEMP_HP)
            {
                TempHPField.tempHp.set(owner, MAX_BLOCK_AND_TEMP_HP);
            }
        }
    }
}

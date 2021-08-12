package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameUtilities;

public class BlindedPower extends CommonPower implements OnChannelOrbSubscriber, OnEvokeOrbSubscriber
{
    public static final String POWER_ID = CreateFullID(BlindedPower.class);
    public static final int DAMAGE_REDUCTION_LV1 = 3;
    public static final int DAMAGE_REDUCTION_LV2 = 4;

    private final AbstractCreature source;

    public static int GetDamageReduction()
    {
        return GameUtilities.HasOrb(Dark.ORB_ID) ? DAMAGE_REDUCTION_LV2 : DAMAGE_REDUCTION_LV1;
    }

    public BlindedPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, POWER_ID);

        this.source = source;
        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onChannelOrb.Subscribe(this);
        CombatStats.onEvokeOrb.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onChannelOrb.Unsubscribe(this);
        CombatStats.onEvokeOrb.Unsubscribe(this);
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(GetDamageReduction(), Color.RED, c.a);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetDamageReduction());
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ORB_FROST_EVOKE, 1.2f, 1.5f);
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb)
    {
        if (Dark.ORB_ID.equals(orb.ID))
        {
            AbstractDungeon.onModifyPower();
            updateDescription();
        }
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if (Dark.ORB_ID.equals(orb.ID))
        {
            AbstractDungeon.onModifyPower();
            updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive(type == DamageInfo.DamageType.NORMAL ? (damage - GetDamageReduction()) : damage, type);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new BlindedPower(owner, source, amount);
    }
}

package eatyourbeets.powers.orbs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public abstract class OrbAffinityPower<Orb extends AbstractOrb> extends CommonPower
{
    protected static final FieldInfo<Integer> _passiveAmount = JUtils.GetField("basePassiveAmount", AbstractOrb.class);
    protected final HashMap<Orb, Integer> affectedOrbs = new HashMap<>();
    protected final EYBCardTooltip orbTooltip;

    public OrbAffinityPower(String powerID, AbstractCreature owner, EYBCardTooltip orbTooltip, int amount)
    {
        super(owner, powerID);

        this.orbTooltip = orbTooltip;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.powerIcon = new TextureAtlas.AtlasRegion(orbTooltip.icon.getTexture(), 0, 0, 0, 0);

        //TODO: Localization
        this.name = orbTooltip.title + " Affinity";

        updateDescription();
    }

    public abstract Orb Validate(AbstractOrb orb);

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        for (AbstractOrb orb : player.orbs)
        {
            Orb o = Validate(orb);
            if (o != null)
            {
                ApplyEffect(o);
                o.updateDescription();
            }
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        for (AbstractOrb orb : player.orbs)
        {
            Orb o = Validate(orb);
            if (o != null)
            {
                RemoveEffect(o);
            }
        }

        super.stackPower(stackAmount);

        for (AbstractOrb orb : player.orbs)
        {
            Orb o = Validate(orb);
            if (o != null)
            {
                ApplyEffect(o);
                o.updateDescription();
            }
        }
    }

    @Override
    public void onChannel(AbstractOrb orb)
    {
        super.onChannel(orb);

        Orb o = Validate(orb);
        if (o != null)
        {
            ApplyEffect(o);
            o.updateDescription();
        }
    }

    protected void ApplyEffect(Orb orb)
    {
        AddPassive(orb, amount);
        affectedOrbs.put(orb, amount);
    }

    protected void RemoveEffect(Orb orb)
    {
        Integer previous = affectedOrbs.get(orb);
        if (previous != null)
        {
            AddPassive(orb, -previous);
        }
    }

    protected int GetPassive(AbstractOrb orb)
    {
        return _passiveAmount.Get(orb);
    }

    protected void SetPassive(AbstractOrb orb, int value)
    {
        _passiveAmount.Set(orb, value);
    }

    protected int AddPassive(AbstractOrb orb, int value)
    {
        int passive = Math.max(0, GetPassive(orb) + value);
        SetPassive(orb, passive);
        return passive;
    }

    @Override
    public void updateDescription()
    {
        //TODO: Localization
        this.description = JUtils.Format("Increase the passive value of your {0} by {1}", orbTooltip.title, amount);
    }
}